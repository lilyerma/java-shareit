package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.OffsetBasedPageRequest;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;


    @Transactional
    @Override
    public ItemDto create(ItemDto itemDto, long ownerId) {
        checkUserExists(ownerId);
        if (itemDto.getRequestId() != null) {
            checkItemRequestExists(itemDto.getRequestId());
        }
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setOwner(ownerId);
        Item itemToReturn = itemRepository.save(item);
        return ItemMapper.toItemDto(itemToReturn);
    }


    //Проверяет, что объект сущестует и что пользователь его владелец
    @Override
    public Boolean checkOwnership(long itemId, long ownerId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundException("не найден такой объект");
        }
        Item existItem = itemRepository.getReferenceById(itemId);

        return existItem.getOwner() == ownerId;
    }

    @Transactional
    @Override
    public ItemDto update(ItemUpdateDto itemDto, long itemId, long ownerId) {
        checkUserExists(ownerId);
        Item item = ItemMapper.fromItemUpdDto(itemDto);
        try {
            Item existItem = itemRepository.getReferenceById(itemId);
            if (existItem.getOwner() != ownerId) {
                throw new NotFoundException("Вещь у этого пользователя не найдена");
            }
            if (item.getName() != null) {
                existItem.setName(item.getName());
            }
            if (item.getDescription() != null) {
                existItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                existItem.setAvailable(item.getAvailable());
            }
            if (itemDto.getIdRequest() != null) {
               checkItemRequestExists(itemDto.getIdRequest());
               existItem.setRequestId(itemDto.getIdRequest());
            }
            Item itemToReturn = itemRepository.save(existItem);
            return ItemMapper.toItemDto(itemToReturn);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("нет предмета с таким номером");
        }
    }

    @Override
    public void delete(long id, long ownerId) {
        checkUserExists(ownerId);
        Item existItem = itemRepository.getReferenceById(id);
        if (existItem.getOwner() != ownerId) {
            throw new NotFoundException("Вещь у этого пользователя не найдена");
        }
        itemRepository.deleteById(id);
    }


    @Override
    public long getOwnerByItemId(long itemId) {
        return itemRepository.getItemById(itemId).getOwner();
    }

    @Override
    public void deleteByOwner(long ownerId) {
        itemRepository.deleteAllByOwner(ownerId);
    }

    @Override
    public List<ItemDto> getUserItems(long id, int from, int size) {
        if (size ==0){
            return new ArrayList<>();
        }
        Pageable pageable = new OffsetBasedPageRequest(size,from, Sort.by(Sort.Direction.ASC,"id"));
        return itemRepository.findItemsByOwner(id,pageable)
                .stream()
                .map(ItemMapper::toItemDto)
                .map(this::addBookingDates)
                .collect(Collectors.toList());
    }

    //Пороверяет, что предмет существует и доступен
    @Override
    public boolean checkAvailable(long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundException("не найден такой предмет");
        }
        if (!itemRepository.getReferenceById(itemId).getAvailable()) {
            throw new ValidationException("Объект недоступен для бронирования");
        }
        return false;
    }

    // метод для добавления даты последнего бронирования и следующего
    @Override
    public ItemDto addBookingDates(ItemDto itemDtoBooking) {
        long itemId = itemDtoBooking.getId();
        ArrayList<Booking> bookings =
                (ArrayList<Booking>) bookingRepository.findBookingByItemIdTwoNext(itemId,
                        LocalDateTime.now());
        if (bookings.size() == 2) {
            Booking bookingLast = bookings.get(0);
            itemDtoBooking.setLastBooking(BookingMapper.toBookingDtoFromBooking(bookingLast));
            Booking bookingNext = bookings.get(1);
            itemDtoBooking.setNextBooking(BookingMapper.toBookingDtoFromBooking(bookingNext));
        } else if (bookings.size() == 1) {
            ArrayList<Booking> bookingPast = (ArrayList<Booking>) bookingRepository.findBookingByItemIdTwoLast(itemId,
                    LocalDateTime.now());
            if (bookingPast.size() > 0) {
                itemDtoBooking.setLastBooking(BookingMapper
                        .toBookingDtoFromBooking(bookingPast.get(0)));
                Booking bookingNext = bookings.get(0);
                itemDtoBooking.setNextBooking(BookingMapper.toBookingDtoFromBooking(bookingNext));
            }
            Booking bookingLast = bookings.get(0);
            itemDtoBooking.setNextBooking(BookingMapper.toBookingDtoFromBooking(bookingLast));
        }
        return itemDtoBooking;
    }

    //Ищем по имени и описанию
    @Override
    public List<ItemDto> searchNameAndDesc(String text, int from, int size) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        Pageable pageable = new OffsetBasedPageRequest(size, from, Sort.by(Sort.DEFAULT_DIRECTION,"id"));
        return itemRepository.search(text, text, pageable)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    // Метод для получения одного предмета для владельца и для пользователя
    @Override
    public ItemDto getItemByIdForOwnerOrForUser(long id, long ownerId) {
        if (checkOwnership(id, ownerId)) {
            return getItemByIdWithBookings(id); // Возвращает владельцу с данными о бронировании
        }
        ItemDto forUsers = getItemById(id);
        addComments(id, forUsers);
        return forUsers;
    }

    @Override
    public ItemDto getItemById(long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundException("объект не найден");
        }
        Item item = itemRepository.getReferenceById(itemId);
        return ItemMapper.toItemDto(item);
    }

    //Добавляем комментарии в DTO объекта и имя пользователя
    private void addComments(long itemId, ItemDto itemDto) {
            ArrayList<Comment> commentsList = (ArrayList<Comment>) commentRepository.findCommentByItemId(itemId);
            ArrayList<CommentDto> commentDtos = (ArrayList<CommentDto>) commentsList.stream()
                    .map(this::makeDTOAndSetName)
                    .collect(Collectors.toList());
            itemDto.setComments(commentDtos);
    }

    //Мапим в DTO и добавляем имя пользователя
    private CommentDto makeDTOAndSetName(Comment comment) {
        String name = userRepository.getReferenceById(comment.getAuthorId()).getName();
        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        commentDto.setAuthorName(name);
        return commentDto;
    }


    //Метод для добавления бронирований к предмету
    @Override
    public ItemDto getItemByIdWithBookings(long id) {
        if(itemRepository.findById(id).isPresent()){
            Item item = itemRepository.getReferenceById(id);
            ItemDto itemDtoBooking = ItemMapper.toItemDto(item);
            addComments(id, itemDtoBooking);
            return addBookingDates(itemDtoBooking);
        } else  {
            throw new NotFoundException("нет предмета с таким id");
        }

    }

    //Метод для создания комментария
    @Override
    @Transactional
    public CommentDto createComment(long itemId, long author, CommentDto commentDto) {
        if (bookingRepository.findOneByBookingByBookerAndItemIdAndStartBeforeAndStatus(author, itemId,
                LocalDateTime.now()).isPresent()) {
            String text = commentDto.getText();
            Comment comment = new Comment(text, author, itemId);
            Comment created = commentRepository.save(comment);
            return makeDTOAndSetName(created);
        } else {
            throw new ValidationException("Не найдены согласованные бронирования в прошлом");
        }
    }

    // Проверка, что пользователь существует
    private void checkUserExists(long userId){
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Не найден пользователь");
        }
    }

    //Проверка, что запрос с таким номером есть
    private void checkItemRequestExists(long requestId){
        if (itemRequestRepository.findById(requestId).isEmpty()) {
            throw new NotFoundException("Не найдена заявка");
        }
    }

}
