package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoNames;

import java.util.List;

public interface BookingService {

    void checkNotOwnerAndAvailable(BookingDto bookingDto, long requestorId);

    BookingDtoNames create(BookingDto bookingDto, long requestorId);

    BookingDtoNames updateStatus(long bookingId, boolean approval, long owner);

    boolean checkExist(long id);

    BookingDtoNames getById(long bookingId, long userId);

    List<BookingDtoNames> checkInputStateAndGetForOwnerByState(String stateStr, long ownerId, int from, int size);

    List<BookingDtoNames> checkInputStateAndGetForBookingUserByState(String stateStr, long ownerId, int from, int size);
}
