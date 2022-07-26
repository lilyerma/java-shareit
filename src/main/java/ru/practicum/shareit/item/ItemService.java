package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, long ownerId);

    ItemDto update(ItemUpdateDto itemDto, long id, long ownerId);

    void delete(long id, long ownerId);

    long getOwnerByItemId(long itemId);

    void deleteByOwner(long ownerId);

    List<ItemDto> getUserItems(long id, int from, int size);

    boolean checkAvailable(long itemId);

    ItemDto addBookingDates(ItemDto itemDto);

    Boolean checkOwnership(long ownerId, long itemId);

    //Ищем по имени и описанию
    List<ItemDto> searchNameAndDesc(String text, int from, int size);

    // Метод для получения одного предмета
    ItemDto getItemByIdForOwnerOrForUser(long id, long ownerId);

    ItemDto getItemById(long itemId);

    ItemDto getItemByIdWithBookings(long id);

    //Метод для создания комментария
    CommentDto createComment(long itemId, long author, CommentDto commentDto);
}
