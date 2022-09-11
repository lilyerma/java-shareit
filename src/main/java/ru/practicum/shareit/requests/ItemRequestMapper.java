package ru.practicum.shareit.requests;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.ItemView;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import java.util.List;

@Component
public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDtoWithItems(ItemRequest itemRequest, List<ItemView> items){
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor(),
                itemRequest.getCreated()
        );
        itemRequestDto.setItems(items);
        return  itemRequestDto;
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest){
        return  new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor(),
                itemRequest.getCreated()
        );
    }


    public static ItemRequest fromItemRequestDto(ItemRequestDto itemRequestDto, long requestorId){
        return new ItemRequest(
                itemRequestDto.getDescription(),
                requestorId
        );
    }

}
