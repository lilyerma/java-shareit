package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;


@Controller
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    // Метод, который добавляет нового пользователя
    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        log.debug("Получен POST-запрос к эндпоинту: '/users' на добавление пользователя");
        return userClient.create(userDto);
    }

    // Метод, который обновляет информацию по существующему пользователю или создает и добавляет нового пользователя
    @ResponseBody
    @PatchMapping ("/{id}")
    public ResponseEntity<Object> update(@PathVariable long id, @RequestBody UserDto userDto) {
        log.debug("Passed params to update method userid " + id);
        return userClient.update(id, userDto);
    }

    // Метод удаляющий пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id) {
        log.debug("Passed params to delete method id " + id);
        return userClient.delete(id);
    }

    // Метод по получению всех пользователей
    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.debug("Requested list of users");
        return userClient.getUsers();
    }

    // Метод по получению одного пользователя (переменная пути)
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable long id) {
        log.debug("Get user method passed params is " + id);
        return userClient.getUserById(id);
    }



}
