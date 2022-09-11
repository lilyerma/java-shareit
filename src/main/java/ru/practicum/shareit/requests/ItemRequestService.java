package ru.practicum.shareit.requests;


import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto getRequestByID(Long userId, long requestId);

    List<ItemRequestDto> getRequestListByPages(Long userId, int from, int size);

    List<ItemRequestDto> getRequestListByRequestor(Long requestorId);

    ItemRequestDto create(ItemRequestDto itemRequestDto, Long requestorId);
}
