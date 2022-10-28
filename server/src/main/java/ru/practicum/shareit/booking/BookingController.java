package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoNames;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoNames create(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                  @RequestBody BookingDto bookingDto)
            throws RuntimeException {
        return bookingService.create(bookingDto, requestorId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoNames update(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long bookingId,
                                  @RequestParam(name = "approved") Boolean approved)
            throws RuntimeException {
        return bookingService.updateStatus(bookingId, approved, ownerId);
    }

    // Владелец получает по бронированиям на свои объекты и по состоянию
    @GetMapping("/owner")
    public List<BookingDtoNames> getUserItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateStr,
                                              @RequestParam(name = "from", defaultValue = "0") int from,
                                              @RequestParam(name = "size", defaultValue = "10") int size)
            throws RuntimeException {
        return bookingService.checkInputStateAndGetForOwnerByState(stateStr, ownerId, from, size);
    }

    // Метод по получению одного объекта
    @GetMapping("/{bookingId}")
    public BookingDtoNames getOne(@RequestHeader("X-Sharer-User-Id") Long user, @PathVariable long bookingId)
            throws RuntimeException {
        return bookingService.getById(bookingId, user);

    }

    // Пользователь получает по своим бронированиям и по состоянию

    @GetMapping
    public List<BookingDtoNames> getBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long user,
                                                   @RequestParam(name = "state", defaultValue = "ALL")
                                                   String stateStr,
                                                   @RequestParam(name = "from", defaultValue = "0") int from,
                                                   @RequestParam(name = "size", defaultValue = "10") int size)
            throws RuntimeException {
        return bookingService.checkInputStateAndGetForBookingUserByState(stateStr, user, from, size);
    }
}