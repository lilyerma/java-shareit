package ru.practicum.shareit.item;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class ItemServiceImpl implements ItemService {

    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(UserStorage userStorage, ItemStorage itemStorage, ItemMapper itemMapper) {
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto create(ItemDto itemDto, long ownerId) {
        System.out.println("Создаем объект");
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setOwner(ownerId);
        Item itemToReturn = itemStorage.create(item);
        return ItemMapper.toItemDto(itemToReturn);
    }

    @Override
    public ItemDto update(ItemUpdateDto itemDto, long id, long ownerId) {
        System.out.println("Обновляем объект");
        Item item = ItemMapper.fromItemUpdDto(itemDto);
        Item existItem = itemStorage.getItemById(id);
        if (!existItem.getOwner().equals(ownerId)) {
            throw new NotFoundException("Вещь у этого пользователя не найдена");
        }
        if (item.getName() != null) {
            existItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            existItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            existItem.setAvailable(item.getAvailable());
        }
        Item itemToReturn = itemStorage.update(existItem);
        return ItemMapper.toItemDto(itemToReturn);
    }

    @Override
    public void delete(long id, long ownerId) {
        Item existItem = itemStorage.getItemById(id);
        if (!existItem.getOwner().equals(ownerId)) {
            throw new NotFoundException("Вещь у этого пользователя не найдена");
        }
        itemStorage.delete(id);
    }

    @Override
    public void deleteByOwner(long ownerId) {
        itemStorage.deleteByOwner(ownerId);
    }

    @Override
    public List<ItemDto> getUserItems(long id) {
        return  itemStorage.getUserItems(id)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchNameAndDesc(String text) {
        return  itemStorage.search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(long id) {
        Item item = itemStorage.getItemById(id);
        return ItemMapper.toItemDto(item);
    }

}
