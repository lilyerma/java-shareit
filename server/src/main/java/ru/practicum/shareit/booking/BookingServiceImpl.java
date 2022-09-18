package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoNames;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ArgumentException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.OffsetBasedPageRequest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public void checkNotOwnerAndAvailable(BookingDto bookingDto, long requestorId) {
        itemService.checkAvailable(bookingDto.getItemId());
        if (itemService.checkOwnership(bookingDto.getItemId(), requestorId)) {
            throw new NotFoundException("нельзя бронировать свои вещи");
        }
        if (userRepository.findById(requestorId).isEmpty()) {
            throw new NotFoundException("нет пользователя");
        }
    }

    @Override
    @Transactional
    public BookingDtoNames create(BookingDto bookingDto, long requestorId) {
        checkNotOwnerAndAvailable(bookingDto, requestorId);
        Booking booking = BookingMapper.fromBookingDto(bookingDto);
        booking.setBooker(requestorId);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        BookingDtoNames bookingDtoReturn = BookingMapper.toBookingDto(booking);
        return addItemName(bookingDtoReturn);
    }

    @Override
    @Transactional
    public BookingDtoNames updateStatus(long bookingId, boolean approval, long owner) {
        long itemId = getById(bookingId, owner).getItem().getId();
        if (!itemService.checkOwnership(itemId, owner)) {
            throw new NotFoundException("Вещь у этого пользователя не найдена");
        }
        Booking booking = bookingRepository.getReferenceById(bookingId);
        if (approval && !booking.getStatus().equals(Status.APPROVED)) {
            booking.setStatus(Status.APPROVED);
        } else if (!approval && !booking.getStatus().equals(Status.REJECTED)) {
            booking.setStatus(Status.REJECTED);
        } else {
            throw new ValidationException("уже есть такой статус");
        }
        bookingRepository.save(booking);
        Booking bookingToReturn = bookingRepository.getReferenceById(bookingId);
        BookingDtoNames bookingDtoReturn = BookingMapper.toBookingDto(bookingToReturn);
        return addItemName(bookingDtoReturn);
    }

    @Override
    public boolean checkExist(long id) {
        if (bookingRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Не найдено бронирование");
        }
        return true;
    }

    public boolean checkOwnerOrRequestor(Long user, Booking booking) {
        if (userRepository.findById(user).isEmpty()) {
            throw new NotFoundException("нет пользователя");
        }
        checkExist(booking.getId());
        long requestorId = booking.getBooker();
        long itemId = booking.getItemId();
        long ownerId = itemService.getOwnerByItemId(itemId);
        if (requestorId == user || ownerId == user) {
            return true;
        } else {
            throw new NotFoundException("не найдено бронирование у этого пользователя");
        }
    }

    @Override
    public BookingDtoNames getById(long bookingId, long user) {
        Booking bookingToReturn = bookingRepository.getReferenceById(bookingId);
        if (checkOwnerOrRequestor(user, bookingToReturn)) {
            BookingDtoNames bookingDtoReturn = BookingMapper.toBookingDto(bookingToReturn);
            return addItemName(bookingDtoReturn);
        } else {
            throw new ConflictException("Прользователь не может запрашивать информацию");
        }
    }


    public List<BookingDtoNames> getByState(long requestorId, State state, int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(size,from, Sort.by(Sort.Direction.DESC,"id"));
        if (bookingRepository.findByBookerOrderByIdDesc(requestorId) == null) {
            return new ArrayList<>();
        }
        switch (state) {
            case PAST:
                return bookingRepository.findByBookerAndEndBeforeOrderByIdDesc(requestorId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findByBookerAndStartAfterOrderByIdDesc(requestorId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findByBookerAndStartBeforeAndEndAfterOrderByIdDesc(requestorId,
                                LocalDateTime.now(), LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findByBookerAndStatus(requestorId, Status.WAITING, pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findByBookerAndStatus(requestorId, Status.REJECTED, pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case ALL:
                return bookingRepository.findByBookerOrderByIdDescPagable(requestorId, pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            default:
                throw new MissingFormatArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public List<BookingDtoNames> getForOwnerByState(long ownerId, State state, int from, int size) {
        if (itemRepository.getItemByOwner(ownerId).size()==0) {
            throw new NotFoundException("Не нашлось bookings");
        }
        Pageable pageable = new OffsetBasedPageRequest(size,from, Sort.by(Sort.Direction.DESC,"id"));

        switch (state) {
            case PAST:
                return bookingRepository.findBookingByOwnerAndStatePast(ownerId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingByOwnerAndStateFuture(ownerId, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingByOwnerAndStateCurrent(ownerId,
                                LocalDateTime.now(),pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingByOwnerAndState(ownerId, Status.WAITING, pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingByOwnerAndState(ownerId, Status.REJECTED, pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case ALL:
                return bookingRepository.findBookingByOwnerAndNotRejected(ownerId,pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            default:
                throw new MissingFormatArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public BookingDtoNames addItemName(BookingDtoNames bookingDto) {
        long itemId = bookingDto.getItem().getId();
        String itemName = itemService.getItemById(itemId).getName();
        bookingDto.getItem().setName(itemName);
        return bookingDto;
    }

    @Override
    public List<BookingDtoNames> checkInputStateAndGetForOwnerByState(String stateStr, long ownerId,
                                                                      int from, int size) {
        try {
            State state = State.valueOf(stateStr.toUpperCase());
            return getForOwnerByState(ownerId, state,from,size);
        } catch (IllegalArgumentException e) {
            throw new ArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingDtoNames> checkInputStateAndGetForBookingUserByState(String stateStr, long user,
                                                                            int from, int size) {
        if (userRepository.findById(user).isEmpty()) {
            throw new NotFoundException("Не найден пользователь");
        }
        try {
            State state = State.valueOf(stateStr.toUpperCase());
            return getByState(user, state,from,size);
        } catch (IllegalArgumentException e) {
            throw new ArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
