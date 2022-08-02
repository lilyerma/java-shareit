package ru.practicum.shareit.item;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import java.util.List;

@Service
@Data
public class ItemServiceImpl implements ItemService {

    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper = new ItemMapper();

    @Autowired
    public ItemServiceImpl(UserStorage userStorage, ItemStorage itemStorage) {
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
    }

    @Override
    public Item create(Item item) {
        return itemStorage.create(item);
    }

    @Override
    public Item update(Item item) {
        Item existItem = itemStorage.getItemById(item.getId());
        if (!existItem.getOwner().equals(item.getOwner())) {
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
        return itemStorage.update(existItem);
    }

    @Override
    public List<Item> getUserItems(long id) {
       return itemStorage.getUserItems(id);
    }

    @Override
    public List<Item> searchNameAndDesc(String text) {
        return  itemStorage.search(text);
    }

    @Override
    public Item getItemById(long id) {
        return itemStorage.getItemById(id);
    }

}
