package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

//Этот DTO для подставновки в BoookingDTO
@Data
@NoArgsConstructor
public class ItemDtoShort {
    private long id;
    @NotNull
    @NotEmpty
    private String name;

    public ItemDtoShort(long id, String name) {
        this.id = id;
        this.name = name;
    }

}
