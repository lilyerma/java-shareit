package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    // Метод, который добавляет новый запрос
    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                 @RequestBody ItemRequestDto itemRequestDto)
            throws RuntimeException {
        return itemRequestService.create(itemRequestDto, requestorId);
    }

    // Метод, который возвращает свои запросы и ответы на них
    @GetMapping
    public List<ItemRequestDto> getRequestList(@RequestHeader("X-Sharer-User-Id") Long requestorId)
            throws RuntimeException {
        return itemRequestService.getRequestListByRequestor(requestorId);
    }

    // Пользователь получает запросы постранично с заданного индекса и в нужном количестве, от новых к старым
    @GetMapping("/all")
    public List<ItemRequestDto> getRequestListByPages(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam (name="from", defaultValue = "0") int from,
                                                      @RequestParam (name="size", defaultValue = "10") int size)
            throws RuntimeException {
        return itemRequestService.getRequestListByPages(userId, from, size);
    }

    // Пользователь получает инфо по конкретному запросу
    @GetMapping("{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable long requestId)
            throws RuntimeException {
        return itemRequestService.getRequestByID(userId, requestId);
    }
}