package ru.practicum.shareit.requests.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.ItemView;

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
    String description;

    private long requestorId;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;

    private List<ItemView> items;

    public ItemRequestDto(long id, String description, long requestorId, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;
        this.created = created;
    }
}
