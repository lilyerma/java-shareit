package ru.practicum.shareit.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.ItemView;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl service;

    private final ObjectMapper mapper = new ObjectMapper();

    ItemRequest itemRequest1;
    ItemRequest itemRequest2;

    private ItemView itemView1;

    private ItemView itemView2;


    @BeforeEach
    void setUp() {
        itemView1 = new ItemView() {
            @Override
            public long getId() {
                return 1L;
            }

            @Override
            public String getName() {
                return "screw";
            }

            @Override
            public String getDescription() {
                return "good screw";
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
        itemView2 = new ItemView() {
            @Override
            public long getId() {
                return 1L;
            }

            @Override
            public String getName() {
                return "hummer";
            }

            @Override
            public String getDescription() {
                return "good hummer";
            }

            @Override
            public Boolean getAvailable() {
                return true;
            }

            @Override
            public Long getRequestId() {
                return 2L;
            }
        };

        itemRequest1 = new ItemRequest("need screw", 1L);
        itemRequest1.setId(2L);
        itemRequest2 = new ItemRequest("need hummer", 2L);
        itemRequest2.setId(3L);
    }

    @Test
    void getRequestByID() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findById(2L)).thenReturn(Optional.of(new ItemRequest()));
        when(itemRequestRepository.getReferenceById(anyLong())).thenReturn(itemRequest1);
        when(itemRepository.findProjectionsByRequestId(anyLong())).thenReturn(Arrays.asList(itemView1, itemView2));
        assertTrue(service.getRequestByID(1L, 2L) instanceof ItemRequestDto);
        assertThrows(NotFoundException.class, () -> service.getRequestByID(2L, 2L));
        assertThrows(NotFoundException.class, () -> service.getRequestByID(1L, 3L));
    }

    @Test
    void getRequestListByPages() {
        List<ItemRequest> itemRequests = Arrays.asList(itemRequest1, itemRequest2);
        Page<ItemRequest> pagedResponse = new PageImpl(itemRequests);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findAll((Pageable) any())).thenReturn(pagedResponse);
        when(itemRepository.findProjectionsByRequestId(anyLong())).thenReturn(Arrays.asList(itemView2, itemView1));
        assertEquals(1, service.getRequestListByPages(1L, 1, 10).size());
        assertThrows(NotFoundException.class, () -> service.getRequestListByPages(12L, 1, 10));
    }

    @Test
    void getRequestListByRequestor() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.getItemRequestByRequestorOrderByCreatedDesc(1L)).thenReturn(Arrays.asList(itemRequest1, itemRequest2));
        when(itemRepository.findProjectionsByRequestId(anyLong())).thenReturn(Arrays.asList(itemView2, itemView1));
        assertEquals(2, service.getRequestListByRequestor(1L).size());
        assertThrows(NotFoundException.class, () -> service.getRequestListByRequestor(2L));
    }


    @Test
    void create() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setRequestorId(1L);
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("need screw");
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest1);
        assertTrue(service.create(itemRequestDto, 1L) instanceof ItemRequestDto);
        assertEquals("need screw", service.create(itemRequestDto, 1L).getDescription());
    }

}
