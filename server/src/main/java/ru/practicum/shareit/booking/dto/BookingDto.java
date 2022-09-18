package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingDto {

    private long id;
    @NotNull
    @Future
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;
    @NotNull
    private Long itemId;
    private Status status;
    private long bookerId;


    public BookingDto(long id, LocalDateTime start, LocalDateTime end, long bookerId) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.bookerId = bookerId;
    }

}
