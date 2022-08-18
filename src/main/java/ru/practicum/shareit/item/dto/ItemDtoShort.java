package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

//Этот DTO для подставновки в BoookingDTO
@Data
@NoArgsConstructor
public class ItemDtoShort {
    long id;
    @NotNull
    @NotEmpty
    String name;

    public ItemDtoShort(long id, String name) {
        this.id = id;
        this.name = name;
    }

}
