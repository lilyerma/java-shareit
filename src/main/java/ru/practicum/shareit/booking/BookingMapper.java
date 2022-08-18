package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoNames;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.user.dto.BookerDto;

@Component
public class BookingMapper {

    // Это маппер для списков, чтобы выдавало в формате под тесты
    public static BookingDtoNames toBookingDto(Booking booking) {
        BookingDtoNames bookingDto = new BookingDtoNames(
                booking.getStart(),
                booking.getEnd()
        );
        bookingDto.setId(booking.getId());
        bookingDto.setBooker(new BookerDto(booking.getBooker()));
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setItem(new ItemDtoShort(booking.getItemId(), null));
        return bookingDto;
    }


    public static Booking fromBookingDto(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItemId()
        );
    }

    public static BookingDto toBookingDtoFromBooking(Booking booking) {
        BookingDto bookingDto = new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker()
        );
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

}
