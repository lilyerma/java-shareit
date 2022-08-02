package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryItemStorage implements ItemStorage {

    Map<Long, Item> mapOfAllItems = new HashMap<>();
    Long id = 0L;

    @Override
    public Item create(Item item) {
        item.setId(id + 1);
        id += 1;
        try {
            mapOfAllItems.put(item.getId(), item);
            log.debug("Новый объект успешно создан/добавлен");
            return mapOfAllItems.get(item.getId());
        } catch (RuntimeException e) {
            log.debug("При попытке создать новый объект произошла внутренняя ошибка сервера");
            throw new RuntimeException("Внутреняя ошибка сервера");
        }
    }

    @Override
    public Item update(Item item) throws RuntimeException {
        try {
            log.debug("Обновляем информацию по объекту через ID");
            mapOfAllItems.put(item.getId(), item);
            return mapOfAllItems.get(item.getId());
        } catch (RuntimeException e) {
            log.debug("При обновлении объекта возникла внутренняя ошибка сервера");
            throw new RuntimeException("Внутреняя ошибка сервера");
        }
    }


    @Override
    public void delete(long id) throws RuntimeException {
        if (id < 0 || mapOfAllItems.get(id) == null) {
            log.debug("При удалении объекта возникла ошибка с ID");
            throw new ValidationException("Ошибка валидации");
        } else {
            try {
                log.debug("Пытаемся удалить объект");
                mapOfAllItems.remove(id);
            } catch (RuntimeException e) {
                log.debug("При удалении объетка возникла внутренняя ошибка сервера");
                throw new RuntimeException("Внутреняя ошибка сервера");
            }
        }
    }

    @Override
    public List<Item> getUserItems(long id) throws RuntimeException {
        try {
            log.debug("Пытаемся вернуть список всех объектов пользователя");
            ArrayList<Item> userItems = (ArrayList<Item>) mapOfAllItems.values().stream()
                    .filter(Item -> Item.getOwner() == id)
                    .collect(Collectors.toList());
            return userItems;
        } catch (RuntimeException exception) {
            log.debug("При попытке вернуть список  объектов возникла внутренняя ошибка сервера");
            throw new RuntimeException("Внутреняя ошибка сервера");
        }
    }

    @Override
    public List<Item> search(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        String caps = text.toUpperCase();
        ArrayList<Item> searchResult = (ArrayList<Item>) mapOfAllItems.values().stream()
                .filter(Item -> Item.getName().toUpperCase().contains(caps) ||
                        Item.getDescription().toUpperCase().contains(caps))
                .filter(Item -> Item.getAvailable() == Boolean.TRUE)
                .collect(Collectors.toList());
        return searchResult;
    }

    @Override
    public Item getItemById(long id) throws RuntimeException {
        if (id < 0) {
            log.debug("При попытке вернуть объект возникла ошибка с ID");
            throw new NotFoundException("Искомый объект не найден");
        }
        if (mapOfAllItems.get(id) == null) {
            log.debug("При получения объекта возникла ошибка с NULL");
            throw new NotFoundException("Объект не найден");
        } else {
            try {
                log.debug("Пытаюсь вернуть объект");
                return mapOfAllItems.get(id);
            } catch (RuntimeException e) {
                log.debug("При попытке вернуть объект возникла внутренняя ошибка сервера");
                throw new RuntimeException("Внутреняя ошибка сервера");
            }
        }
    }
}
