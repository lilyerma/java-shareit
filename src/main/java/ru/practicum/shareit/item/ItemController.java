package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;
    private final ItemMapper itemMapper = new ItemMapper();

    @Autowired
    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    // Метод, который добавляет новый объект
    @PostMapping
    public Item create(@RequestHeader("X-Sharer-User-Id") Long ownerId, @Valid @RequestBody ItemDto itemDto)
            throws RuntimeException {
        System.out.println("Создаем объект");
        userService.checkId(ownerId);
        Item item = itemMapper.fromItemDto(itemDto);
        item.setOwner(ownerId);
        return itemService.create(item);
        }

    @PatchMapping("/{id}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id,
                       @Valid @RequestBody ItemUpdateDto itemDto)
            throws RuntimeException {
        System.out.println("Обновляем объект");
        userService.checkId(ownerId);
        Item item = itemMapper.fromItemUpdDto(itemDto);
        item.setOwner(ownerId);
        item.setId(id);
        return itemService.update(item);
    }

//    @DeleteMapping("/{id}")
//    public void delete(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id) throws RuntimeException {
//        itemService.delete(id);
//    }

    // Метод по получению всех объектов пользователя
    @GetMapping
    public List<Item> getAll(@RequestHeader("X-Sharer-User-Id") Long ownerId) throws RuntimeException {
        return itemService.getUserItems(ownerId);
    }

    // Метод по получению одного объекта
    @GetMapping("/{id}")
    public Item getOne(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id) throws RuntimeException {
        return itemService.getItemById(id);
    }

    @GetMapping("/search")
    public List<Item> searchNameAndDesc(@RequestParam String text) throws RuntimeException {
        return itemService.searchNameAndDesc(text);
    }

    }


