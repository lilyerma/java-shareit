package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Item item);

    Item update(Item item);

    List<Item> getUserItems(long id);

    List<Item> searchNameAndDesc(String text);

    Item getItemById(long id);

}
