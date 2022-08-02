package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * // TODO .
 */

@Data
public class ItemDto {

    long id;
    @NotNull
    @NotEmpty
    String name;
    @NotNull
    @NotEmpty
    String description;
    @NotNull
    Boolean available;
    Long idRequest;

    public ItemDto(String name, String description, Boolean available, Long idRequest) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
