package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoGw;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoGw {

    long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String description;
    @NotNull
    private Boolean available;
    private Long requestId;
    private BookingDtoGw lastBooking;
    private BookingDtoGw nextBooking;
    private List<CommentDtoGw> comments;

    public ItemDtoGw(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }


}
