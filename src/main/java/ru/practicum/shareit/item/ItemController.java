package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    // Метод, который добавляет новый объект
    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long ownerId, @Valid @RequestBody ItemDto itemDto)
            throws RuntimeException {
        System.out.println("Создаем объект");
        userService.checkId(ownerId);
        return itemService.create(itemDto, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id,
                          @Valid @RequestBody ItemUpdateDto itemDto)
            throws RuntimeException {
        userService.checkId(ownerId);
        return itemService.update(itemDto, id, ownerId);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id) throws RuntimeException {
        userService.checkId(ownerId);
        itemService.delete(id, ownerId);
    }

    // Метод по получению всех объектов пользователя
    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) throws RuntimeException {
        return itemService.getUserItems(ownerId);
    }

    // Метод по получению одного объекта
    @GetMapping("/{id}")
    public ItemDto getOne(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id) throws RuntimeException {
        if (itemService.checkOwnership(id, ownerId)) {
            return itemService.getItemByIdWithBookings(id); // Возвращает владельцу с данными о бронировании
        }
        return itemService.getItemById(id); // Возвращает пользователям с отзывами
    }

    @GetMapping("/search")
    public List<ItemDto> searchNameAndDesc(@RequestParam String text) throws RuntimeException {
        return itemService.searchNameAndDesc(text);
    }

    // Метод для добавления комментариев

    @PostMapping ("/{id}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long commenter, @PathVariable long id,
                             @Valid @RequestBody CommentDto commentDto)
            throws RuntimeException {
        return itemService.createComment(id, commenter, commentDto);
    }

}


