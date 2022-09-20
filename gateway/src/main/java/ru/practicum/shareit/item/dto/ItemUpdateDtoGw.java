package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemUpdateDtoGw {

    long id;
    String name;
    String description;
    Boolean available;
    Long idRequest;

}
