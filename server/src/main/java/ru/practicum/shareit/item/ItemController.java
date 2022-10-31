package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // Метод, который добавляет новый объект
    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long ownerId, @RequestBody ItemDto itemDto)
            throws RuntimeException {
        return itemService.create(itemDto, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id,
                          @RequestBody ItemUpdateDto itemDto) throws RuntimeException
    {
        return itemService.update(itemDto, id, ownerId);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id) throws RuntimeException {
        itemService.delete(id, ownerId);
    }

    // Метод по получению всех объектов пользователя
    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @RequestParam(name = "from", defaultValue = "0") int from,
                                      @RequestParam(name = "size", defaultValue = "10") int size)
            throws RuntimeException {
        return itemService.getUserItems(ownerId, from, size);
    }

    // Метод по получению одного объекта
    @GetMapping("/{id}")
    public ItemDto getOne(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id)
            throws RuntimeException {
        return itemService.getItemByIdForOwnerOrForUser(id,ownerId); // Возвращает пользователям с отзывами
    }

    @GetMapping("/search")
    public List<ItemDto> searchNameAndDesc(@RequestParam String text,
                                           @RequestParam(name = "from", defaultValue = "0") int from,
                                           @RequestParam(name = "size", defaultValue = "10") int size)
            throws RuntimeException {
        return itemService.searchNameAndDesc(text, from, size);
    }

    // Метод для добавления комментариев

    @PostMapping ("/{id}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long commenter, @PathVariable long id,
                                 @RequestBody CommentDto commentDto)
            throws RuntimeException {
        return itemService.createComment(id, commenter, commentDto);
    }

}

