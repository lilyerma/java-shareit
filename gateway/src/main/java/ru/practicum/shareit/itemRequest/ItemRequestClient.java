package ru.practicum.shareit.itemRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> create(Long requestorId, ItemRequestDto itemRequestDto) {
        return post("", requestorId, itemRequestDto);
    }

    // Метод, который возвращает свои запросы и ответы на них

    public ResponseEntity<Object> getRequestListByRequestor(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return get("", requestorId);
    }

    // Пользователь получает запросы постранично с заданного индекса и в нужном количестве, от новых к старым

    public ResponseEntity<Object> getRequestListByPages(Long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all", userId, parameters);
    }

    // Пользователь получает инфо по конкретному запросу

    public ResponseEntity<Object> getRequestByID(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable long requestId)
            throws RuntimeException {
        return get("/"+requestId, userId);
    }
}
