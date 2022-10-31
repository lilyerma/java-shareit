package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> create(UserDto userDto) {
        return post("", userDto);
    }

    // Метод, который обновляет информацию по существующему пользователю или создает и добавляет нового пользователя

    public ResponseEntity<Object> update(long id, UserDto userDto){
        return patch("/"+id, userDto);
    }

    // Метод удаляющий пользователя

    public ResponseEntity<Object> delete(@PathVariable long id) {
        return delete("/"+id);
    }

    // Метод по получению всех пользователей

    public ResponseEntity<Object>  getUsers() throws RuntimeException {
        return get("");
    }

    // Метод по получению одного пользователя (переменная пути)

    public ResponseEntity<Object> getUserById(long id) {
        return get("/"+id);
    }

}
