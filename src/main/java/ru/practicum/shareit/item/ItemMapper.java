package ru.practicum.shareit.item;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;


@Component
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable()
                //    item.getRequest() //!= null ? item.getRequest().getId() : null
        );
        itemDto.setId(item.getId());
        return itemDto;
    }
    public static ItemDto toItemDto(Item item, BookingDto last, BookingDto next) {
        ItemDto itemDto = new ItemDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
        itemDto.setId(item.getId());
        return itemDto;
    }


    public static Item fromItemDto(ItemDto itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
    }

    public static ItemUpdateDto toItemUpdDto(Item item) {
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() //!= null ? item.getRequest().getId() : null
        );
        itemUpdateDto.setId(item.getId());
        return itemUpdateDto;
    }

    public static Item fromItemUpdDto(ItemUpdateDto itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
    }

}
