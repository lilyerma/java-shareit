package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoNames;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.exception.ArgumentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public BookingController(BookingService bookingService, UserService userService, ItemService itemService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.itemService = itemService;
    }

    @PostMapping
    public BookingDtoNames create(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                  @Valid @RequestBody BookingDto bookingDto)
            throws RuntimeException {
        itemService.checkAvailable(bookingDto.getItemId());
        if (itemService.checkOwnership(bookingDto.getItemId(), requestorId)) {
            throw new NotFoundException("нельзя бронировать свои вещи");
        }
        System.out.println("Создаем бронирование");
        userService.checkId(requestorId);
        return bookingService.create(bookingDto, requestorId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoNames update(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long bookingId,
                                  @RequestParam(name = "approved") Boolean approved)
            throws RuntimeException {
        long itemId = bookingService.getById(bookingId, ownerId).getItem().getId();
        if (!itemService.checkOwnership(itemId, ownerId)) {
            throw new NotFoundException("Вещь у этого пользователя не найдена");
        }
        return bookingService.updateStatus(bookingId, approved, ownerId);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id) throws RuntimeException {
        userService.checkId(ownerId);
        itemService.delete(id, ownerId);
    }

    // Метод по получению всех объектов пользователя


    @GetMapping("/owner")
    public List<BookingDtoNames> getUserItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                              @RequestParam(name = "state", required = false) String stateStr) throws RuntimeException {
        if (stateStr == null) {
            return bookingService.getForOwnerByState(ownerId, State.ALL);
        }
        try {
            State state = State.valueOf(stateStr.toUpperCase());
            return bookingService.getForOwnerByState(ownerId, state);
        } catch (IllegalArgumentException e) {
            throw new ArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    // Метод по получению одного объекта
    @GetMapping("/{bookingId}")
    public BookingDtoNames getOne(@RequestHeader("X-Sharer-User-Id") Long user, @PathVariable long bookingId)
            throws RuntimeException {
        userService.checkId(user);
        bookingService.checkExist(bookingId);
        return bookingService.getById(bookingId, user);
    }

    @GetMapping
    public List<BookingDtoNames> getBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long user,
                                                   @RequestParam(name = "state", required = false)
                                                   String stateStr) throws RuntimeException {
        userService.checkId(user);
        if (stateStr == null) {
            return bookingService.getByState(user, State.ALL);
        }
        try {
            State state = State.valueOf(stateStr.toUpperCase());
            return bookingService.getByState(user, state);
        } catch (IllegalArgumentException e) {
            throw new ArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
