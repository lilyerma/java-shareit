package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Метод, который добавляет нового пользователя
    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) throws RuntimeException {
        return userService.create(userDto);
    }

    // Метод, который обновляет информацию по существующему пользователю или создает и добавляет нового пользователя
    @PatchMapping ("/{id}")
    public UserDto update(@PathVariable long id, @Valid @RequestBody UserDto userDto) throws RuntimeException {
        return userService.update(userDto, id);
    }

    // Метод удаляющий пользователя
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) throws RuntimeException {
        userService.delete(id);
    }

    // Метод по получению всех пользователей
    @GetMapping
    public List<UserDto> getUsers() throws RuntimeException {
        return userService.getUsers();
    }

    // Метод по получению одного пользователя (переменная пути)
    @GetMapping("/{id}")
    public UserDto getOne(@PathVariable long id) throws RuntimeException {
        return userService.getUserById(id);
    }



}
