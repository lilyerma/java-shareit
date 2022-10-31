package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDtoGw {

    private long id;
    @NotNull
    @NotBlank
    @Size(min =3, max = 350)
    private String text;
    private String authorName;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;

    public CommentDtoGw(long id, String text, LocalDateTime created){
        this.id = id;
        this.text = text;
        this.created = created;
    }

}
