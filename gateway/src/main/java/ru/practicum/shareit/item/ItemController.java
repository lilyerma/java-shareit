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
                                         @Valid @RequestBody ItemDtoGw itemDtoGw){
        log.debug("Post request for item " + itemDtoGw.getName() + " from user " + ownerId);
        return itemClient.create(ownerId, itemDtoGw);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id,
                                         @Valid @RequestBody ItemUpdateDtoGw itemDto)  {
        log.debug("Patch request from user " + ownerId + " for item " + id);
        return itemClient.update(id, ownerId, itemDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id)  {
      log.debug("Delete reuest for the item " + id + " from user " + ownerId);
       return itemClient.delete(id, ownerId);
    }

    // Метод по получению всех объектов пользователя
    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                      @RequestParam(name = "size", defaultValue = "10") @Min(1) int size)
           {
        log.debug("Get request for items of the user " + ownerId);
               return itemClient.getUserItems(ownerId, from, size);
    }

    // Метод по получению одного объекта
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable long id)
            {
        log.debug("Get request for one item " + id + " from the user " + ownerId);
        return itemClient.getItemByIdForOwnerOrForUser(id,ownerId); // Возвращает пользователям с отзывами
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchNameAndDesc(@RequestParam String text,
                                                    @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                                    @RequestParam(name = "size", defaultValue = "10") @Min(1) int size) {
        log.debug("Search request for the text " + text);
        return itemClient.searchNameAndDesc(text, from, size);
    }

    // Метод для добавления комментариев

    @ResponseBody
    @PostMapping ("/{id}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long commenter, @PathVariable long id,
                                 @Valid @RequestBody CommentDtoGw commentDtoGw) {
        log.debug("passed commenter id "+ commenter + " item id "+id);
        return itemClient.createComment(id, commenter, commentDtoGw);
    }

}


