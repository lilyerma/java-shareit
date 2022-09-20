package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoGw;
import ru.practicum.shareit.item.dto.ItemDtoGw;
import ru.practicum.shareit.item.dto.ItemUpdateDtoGw;

import javax.validation.Valid;
import javax.validation.constraints.Min;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    // Метод, который добавляет новый объект
    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                         @Valid @RequestBody ItemDtoGw itemDtoGw)
           {
        return itemClient.create(ownerId, itemDtoGw);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id,
                                         @Valid @RequestBody ItemUpdateDtoGw itemDto)  {
        return itemClient.update(id, ownerId, itemDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id)  {
       return itemClient.delete(id, ownerId);
    }

    // Метод по получению всех объектов пользователя
    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                      @RequestParam(name = "size", defaultValue = "10") @Min(1) int size)
           {
        return itemClient.getUserItems(ownerId, from, size);
    }

    // Метод по получению одного объекта
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id)
            throws RuntimeException {
        return itemClient.getItemByIdForOwnerOrForUser(id,ownerId); // Возвращает пользователям с отзывами
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchNameAndDesc(@RequestParam String text,
                                                    @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                                    @RequestParam(name = "size", defaultValue = "10") @Min(1) int size)
            throws RuntimeException {
        return itemClient.searchNameAndDesc(text, from, size);
    }

    // Метод для добавления комментариев

    @ResponseBody
    @PostMapping ("/{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long commenter, @PathVariable long id,
                                 @Valid @RequestBody CommentDtoGw commentDtoGw)
            throws RuntimeException {
        log.debug("passed commenter id "+ commenter + " item id "+id);
        log.info("passed commenter id "+ commenter + " item id "+id);
        return itemClient.createComment(id, commenter, commentDtoGw);
    }

}


