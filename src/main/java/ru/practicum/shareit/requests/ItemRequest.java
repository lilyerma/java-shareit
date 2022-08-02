package ru.practicum.shareit.requests;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
public class ItemRequest {

    long id;
    String description;
    long requestor;
    LocalDateTime created;

}
