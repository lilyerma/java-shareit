package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDtoGw;
import ru.practicum.shareit.item.dto.ItemDtoGw;
import ru.practicum.shareit.item.dto.ItemUpdateDtoGw;

import javax.validation.Valid;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> create(Long ownerId, ItemDtoGw itemDtoGw) {
        return post("", ownerId, itemDtoGw);
    }

    public ResponseEntity<Object> update(long id, long ownerId,
                          @Valid @RequestBody ItemUpdateDtoGw itemDto) {
        return patch("/"+id, ownerId, itemDto);
    }

    public ResponseEntity<Object> delete(Long ownerId, long id) {
        return delete("/"+id, ownerId);
    }

    // Метод по получению всех объектов пользователя

    public ResponseEntity<Object> getUserItems(Long ownerId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/", ownerId,parameters);
    }

    // Метод по получению одного объекта
    public ResponseEntity<Object> getItemByIdForOwnerOrForUser(long id, Long ownerId) {
        return get("/"+id, ownerId); // Возвращает пользователям с отзывами
    }

    public ResponseEntity<Object> searchNameAndDesc(String text, int from, Integer size) {
        String path = "/search?text=" + text + "&from=" + from;
        if (size != null) {
            path += "&size=" + size;
        }
        return get(path);
    }

    // Метод для добавления комментариев

    public ResponseEntity<Object> createComment(long id, long commenter, CommentDtoGw commentDtoGw) {

        return post("/"+id+"/comment", commenter, null, commentDtoGw);
    }
}
