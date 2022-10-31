package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class ValidBookingValidator implements ConstraintValidator<ValidBooking, Booking> {

    @Override
    public boolean isValid(
            Booking booking, ConstraintValidatorContext context) {

        if (booking == null) {
            return true;
        }

        if (booking.getStart() == null
                || booking.getEnd() == null
        ) {
            return false;
        }

        return (booking.getStart().isAfter(LocalDateTime.now())
                && booking.getStart().isBefore(booking.getEnd()));
    }
}
