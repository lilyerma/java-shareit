package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

  //  private final ItemRequestService itemRequestService;
   private final ItemRequestClient itemRequestClient;

    // Метод, который добавляет новый запрос
    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                 @Valid @RequestBody ItemRequestDto itemRequestDto)
            throws RuntimeException {
        return itemRequestClient.create(requestorId,itemRequestDto);
    }

    // Метод, который возвращает свои запросы и ответы на них
    @GetMapping
    public ResponseEntity<Object> getRequestList(@RequestHeader("X-Sharer-User-Id") Long requestorId)
            throws RuntimeException {
        return itemRequestClient.getRequestListByRequestor(requestorId);
    }

    // Пользователь получает запросы постранично с заданного индекса и в нужном количестве, от новых к старым
    @GetMapping("/all")
    public ResponseEntity<Object> getRequestListByPages(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam (name="from", defaultValue = "0") @Min(0) int from,
                                                      @RequestParam (name="size", defaultValue = "10") @Min(1) int size)
            throws RuntimeException {
        return itemRequestClient.getRequestListByPages(userId, from, size);
    }

    // Пользователь получает инфо по конкретному запросу
    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @PathVariable long requestId)
            throws RuntimeException {
        return itemRequestClient.getRequestByID(userId, requestId);
    }
}
