package ru.practicum.shareit.itemRequest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemViewGw;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemRequestDto {


    private long id;
    @NotNull
    @NotBlank
    @Size(min =3, max = 350)
    private String description;

    private long requestorId;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;

    private List<ItemViewGw> items;

}
