package ru.practicum.shareit.requests;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.ItemView;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ItemRequestMapperTest {

    @InjectMocks
    ItemRequestMapper itemRequestMapper;
    ItemView itemView1;
    ItemRequestDto itemRequestDto1;

    ItemRequest itemRequest;

    @BeforeEach
    void setUp() {

        itemRequest = new ItemRequest("some description", 1L);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setId(1L);
        itemRequest.setRequestor(1L);

        itemRequestDto1 = new ItemRequestDto();
        itemRequestDto1.setDescription("Some description for the request");
        itemRequestDto1.setRequestorId(1L);
        itemRequestDto1.setId(1L);

        itemView1 = new ItemView() {
            @Override
            public long getId() {
                return 1l;
            }

            @Override
            public String getName() {
                return "screw";
            }

            @Override
            public String getDescription() {
                return "standard screw";
            }

            @Override
            public Boolean getAvailable() {
                return true;
            }

            @Override
            public Long getRequestId() {
                return 1L;
            }
        };

    }


    @Test
    void checkItemRequestAndItemViewToItemRequestDtoWithItems() {
        List<ItemView> items = Arrays.asList(itemView1);
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDtoWithItems(itemRequest, items);

        Assert.assertEquals(1L, itemRequestDto.getId());
        Assert.assertEquals("some description", itemRequestDto.getDescription());
        Assert.assertEquals(items, itemRequestDto.getItems());
        Assert.assertEquals(1L, itemRequestDto.getRequestorId());
        Assert.assertEquals(itemView1, items.get(0));
        Assert.assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                itemRequestDto.getCreated().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void checkItemRequestAndItemViewToItemRequestDto() {
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);

        Assert.assertEquals(1L, itemRequestMapper.toItemRequestDto(itemRequest).getId());
        Assert.assertEquals("some description", itemRequestDto.getDescription());
        Assert.assertEquals(1L, itemRequestDto.getRequestorId());
        Assert.assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), itemRequestDto.getCreated().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void checkItemRequestDtoItemRequest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("smart description");

        ItemRequest itemRequest = itemRequestMapper.fromItemRequestDto(itemRequestDto, 1L);

        Assert.assertEquals("smart description",
                itemRequestMapper.fromItemRequestDto(itemRequestDto, 1L).getDescription());
        Assertions.assertEquals(1L, itemRequest.getRequestor());
    }

}


