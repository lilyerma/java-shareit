package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoNames;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    void checkNotOwnerAndAvailable(BookingDto bookingDto, long requestorId);

    BookingDtoNames create(BookingDto bookingDto, long requestorId);

    BookingDtoNames updateStatus(long bookingId, boolean approval, long owner);

    boolean checkExist(long id);

    BookingDtoNames getById(long bookingId, long userId);

    List<BookingDtoNames> getByState(long requestorId, State state);

    List<BookingDtoNames> getForOwnerByState(long ownerId, State state);

    List<BookingDtoNames> checkInputStateAndGetForOwnerByState(String stateStr, long ownerId);

    List<BookingDtoNames> checkInputStateAndGetForBookingUserByState(String stateStr, long ownerId);
}
