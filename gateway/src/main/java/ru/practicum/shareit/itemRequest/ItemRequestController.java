package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

  //  private final ItemRequestService itemRequestService;
   private final ItemRequestClient itemRequestClient;

    // Метод, который добавляет новый запрос
    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.debug("Post request from user "+ requestorId + " for object " + itemRequestDto.getDescription());
        return itemRequestClient.create(requestorId,itemRequestDto);
    }

    // Метод, который возвращает свои запросы и ответы на них
    @GetMapping
    public ResponseEntity<Object> getRequestList(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.debug("Get request for list of the requests by user " + requestorId);
        return itemRequestClient.getRequestListByRequestor(requestorId);
    }

    // Пользователь получает запросы постранично с заданного индекса и в нужном количестве, от новых к старым
    @GetMapping("/all")
    public ResponseEntity<Object> getRequestListByPages(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam (name="from", defaultValue = "0") @Min(0) int from,
                                                      @RequestParam (name="size", defaultValue = "10") @Min(1) int size) {
        log.debug("Get request for all requests by user  " + userId);
        return itemRequestClient.getRequestListByPages(userId, from, size);
    }

    // Пользователь получает инфо по конкретному запросу
    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @PathVariable long requestId){
        log.debug("Get requests fro request " + requestId + " by user " + userId);
        return itemRequestClient.getRequestByID(userId, requestId);
    }
}
