package ru.practicum.shareit.requests;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequest {

    long id;
    String description;
    long requestor;
    LocalDateTime created;

}
