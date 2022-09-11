package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ItemDtoForRequest {

    long id;
    @NotNull
    @NotEmpty
    String name;
    long owner;

    public ItemDtoForRequest(long id, String name, long owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

}
