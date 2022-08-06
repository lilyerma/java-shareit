package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, long ownerId);

    ItemDto update(ItemUpdateDto itemDto, long id, long ownerId);

    void delete(long id, long ownerId);

    void deleteByOwner(long ownerId);

    List<ItemDto> getUserItems(long id);

    List<ItemDto> searchNameAndDesc(String text);

    ItemDto getItemById(long id);

}
