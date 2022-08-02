package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

/**
 * // TODO .
 */
@Data
public class Item {


    long id;
    @NotNull
    String name;
    @NotNull
    String description;
    @NotNull
    Boolean available;
    @NotNull
    Long owner;
    ItemRequest request;

    HashMap <Long,String> comments;

    public Item(){
    }
    public Item(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
