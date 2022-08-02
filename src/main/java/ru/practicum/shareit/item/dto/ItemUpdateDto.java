package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemUpdateDto {

    String name;
    String description;
    Boolean available;
    Long idRequest;

    public ItemUpdateDto(String name, String description, Boolean available, Long idRequest) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
