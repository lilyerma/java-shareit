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
                                         @Valid @RequestBody ItemDtoGw itemDtoGw) {
        log.debug("Passed params to create ownerId" + ownerId);
        return itemClient.create(ownerId, itemDtoGw);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id,
                                         @Valid @RequestBody ItemUpdateDtoGw itemDto) {
        log.debug("Passed params to update ownerId" + ownerId + " item id" + id);
        return itemClient.update(id, ownerId, itemDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id) {
        log.debug("Passed params to delete ownerId" + ownerId + " itemId " + id);
        return itemClient.delete(id, ownerId);
    }

    // Метод по получению всех объектов пользователя
    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                               @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                               @RequestParam(name = "size", defaultValue = "10") @Min(1) int size) {
        log.debug("Passed params to get list items of the user ownerId " + ownerId + " from " + from + " size " + size);
        return itemClient.getUserItems(ownerId, from, size);
    }

    // Метод по получению одного объекта
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id) {
       log.debug("Passed params to get item ownerId " + ownerId + " itemId " + id );
        return itemClient.getItemByIdForOwnerOrForUser(id, ownerId); // Возвращает пользователям с отзывами
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchNameAndDesc(@RequestParam String text, @RequestParam(name = "from",
            defaultValue = "0") @Min(0) int from, @RequestParam(name = "size", defaultValue = "10") @Min(1) int size)  {
        log.debug("Passed params to search method text " + text + " from " + from + " size " + size );
        return itemClient.searchNameAndDesc(text, from, size);
    }

    // Метод для добавления комментариев

    @ResponseBody
    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long commenter,
                                             @PathVariable long id, @Valid @RequestBody CommentDtoGw commentDtoGw) {
        log.debug("Passed to method to create comment commenter id " + commenter + " item id " + id);
        return itemClient.createComment(id, commenter, commentDtoGw);
    }
}


