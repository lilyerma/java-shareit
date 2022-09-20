package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoGw;

import javax.validation.Valid;
import javax.validation.constraints.Min;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                         @Valid @RequestBody BookingDtoGw bookingDtoGw)
            throws RuntimeException {
        return bookingClient.create(requestorId, bookingDtoGw);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long bookingId,
                                  @RequestParam(name = "approved") Boolean approved)
            throws RuntimeException {
        log.debug("переданы параметры, владелец"+ ownerId +" номер бронирования "+ bookingId);
        log.info("переданы параметры, владелец"+ ownerId +" номер бронирования "+ bookingId);
        return bookingClient.updateStatus(bookingId, approved, ownerId);
    }

    // Владелец получает по бронированиям на свои объекты и по состоянию
    @ResponseBody
    @GetMapping("/owner")
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateStr,
                                              @Valid @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                              @Valid @RequestParam(name = "size", defaultValue = "10") @Min(1) int size)
            throws RuntimeException {
        return bookingClient.checkInputStateAndGetForOwnerByState(ownerId,stateStr,from, size);
    }

    // Метод по получению одного объекта
    @ResponseBody
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getOne(@RequestHeader("X-Sharer-User-Id") Long user, @PathVariable long bookingId)
            throws RuntimeException {
        return bookingClient.getById(bookingId, user);
    }

    // Пользователь получает по своим бронированиям и по состоянию
    @ResponseBody
    @GetMapping
    public ResponseEntity<Object> getBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long user,
                                                   @RequestParam(name = "state", defaultValue = "ALL")
                                                   String stateStr,
                                                   @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                                   @RequestParam(name = "size", defaultValue = "10") @Min(1) int size) throws RuntimeException {
        return bookingClient.checkInputStateAndGetForBookingUserByState(user, stateStr, from, size);
    }
}
