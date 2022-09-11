package ru.practicum.shareit.requests;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.ItemView;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.OffsetBasedPageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {


    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto getRequestByID(Long userId, long requestId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (itemRequestRepository.findById(requestId).isEmpty()) {
            throw new NotFoundException("Не найдена такая заявка");
        }
        ItemRequest itemRequest = itemRequestRepository.getReferenceById(requestId);
        return itemRequestDtoWithResponse(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getRequestListByPages(Long userId, int from, int size) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        Pageable page = new OffsetBasedPageRequest(size,from,Sort.by(Sort.Direction.DESC, "created"));

        return itemRequestRepository.findAll(page).getContent().stream()
                .filter(ir -> ir.getRequestor() !=userId)
                .map(this::itemRequestDtoWithResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getRequestListByRequestor(Long requestorId) {
        if (userRepository.findById(requestorId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        return itemRequestRepository.getItemRequestByRequestorOrderByCreatedDesc(requestorId).stream()
                .map(this::itemRequestDtoWithResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ItemRequestDto create(ItemRequestDto itemRequestDto, Long requestorId) {
        if (userRepository.findById(requestorId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        ItemRequest itemRequest = ItemRequestMapper.fromItemRequestDto(itemRequestDto, requestorId);
        ItemRequest newRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(newRequest);
    }

    private ItemRequestDto itemRequestDtoWithResponse(ItemRequest itemRequest) {
        List<ItemView> items = itemRepository.findProjectionsByRequestId(itemRequest.getId());
        return ItemRequestMapper.toItemRequestDtoWithItems(itemRequest, items);
    }
}
