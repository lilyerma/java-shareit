package ru.practicum.shareit.item;
import ru.practicum.shareit.item.model.Item;


import java.util.List;

public interface ItemStorage {

    // Метод по созданию/добавлению нового объекта
    Item create(Item item);

    Item update(Item item)  throws RuntimeException; // Метод обновляющий объект

    // нет, создает нового (Модификация)
    void delete(long id) throws RuntimeException; // Метод удаляющий объект

    List<Item> getUserItems(long id) throws RuntimeException; // Метод по возвращению все объекты


    List<Item> search(String text);

    Item getItemById(long id) throws RuntimeException; // Метод возвращающий объект по ID

    void deleteByOwner(long id);
}
