package ru.practicum.shareit.booking;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoNames;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;

    public BookingServiceImpl(BookingRepository bookingRepository, ItemService itemService) {
        this.bookingRepository = bookingRepository;

        this.itemService = itemService;
    }


    @Override
    @Transactional
    public BookingDtoNames create(BookingDto bookingDto, long requestorId) {
        Booking booking = BookingMapper.fromBookingDto(bookingDto);
        booking.setBooker(requestorId);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        BookingDtoNames bookingDtoReturn = BookingMapper.toBookingDto(booking);
        return addItemName(bookingDtoReturn);
    }

//    @Override
//    public List<BookingDtoNames> findBookingByOwner(long owner) {
//        return bookingRepository.findBookingByOwnerAndNotRejected(owner)
//                .stream()
//                .map(BookingMapper::toBookingDto)
//                .map(BookingDto -> addItemName(BookingDto))
//                .collect(Collectors.toList());
//    }

    @Override
    @Transactional
    public BookingDtoNames updateStatus(long bookingId, boolean approval, long owner) {
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
        if (bookingRepository.findById(id).isEmpty()){
            throw new NotFoundException("Не найдено бронирование");
        }
        return true;
    }

    public boolean checkOwnerOrRequestor(Long user, Booking booking) {
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

    @Override
    public List<BookingDtoNames> getByState(long requestorId, State state) {
        if (bookingRepository.findByBookerOrderByIdDesc(requestorId) == null) {
            return new ArrayList<>();
        }
        switch (state) {
            case PAST:
                return bookingRepository.findByBookerAndEndBeforeOrderByIdDesc(requestorId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findByBookerAndStartAfterOrderByIdDesc(requestorId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findByBookerAndStartBeforeAndEndAfterOrderByIdDesc(requestorId,
                                LocalDateTime.now(), LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findByBookerAndStatusOrderByIdDesc(requestorId, Status.WAITING)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findByBookerAndStatusOrderByIdDesc(requestorId, Status.REJECTED)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case ALL:
                return bookingRepository.findByBookerOrderByIdDesc(requestorId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            default:
                throw new MissingFormatArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingDtoNames> getForOwnerByState(long ownerId, State state) {
//        if (itemRepository.findItemsByOwner(ownerId).size() == 0) {
        if (itemService.getUserItems(ownerId).size() == 0) {
            throw new NotFoundException("Не нашлось bookings");
        }

        switch (state) {
            case PAST:
                return bookingRepository.findBookingByOwnerAndStatePast(ownerId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findBookingByOwnerAndStateFuture(ownerId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findBookingByOwnerAndStateCurrent(ownerId,
                                LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findBookingByOwnerAndState(ownerId, Status.WAITING)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findBookingByOwnerAndState(ownerId, Status.REJECTED)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .map(this::addItemName)
                        .collect(Collectors.toList());
            case ALL:
                return bookingRepository.findBookingByOwnerAndNotRejected(ownerId)
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

}
