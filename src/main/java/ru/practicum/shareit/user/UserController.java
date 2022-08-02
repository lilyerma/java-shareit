package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    // Метод, который добавляет нового пользователя
    @PostMapping
    public User create(@Valid @RequestBody UserDto userDto) throws RuntimeException {
        System.out.println("Создаем пользователя");
        if (userDto.getEmail()==null){
            throw new ValidationException("нельзя чтобы был пустой email");
        }
        User user = userMapper.fromUserDto(userDto);
        return userService.getUserStorage().create(user);
    }

    // Метод, который обновляет информацию по существующему пользователю или создает и добавляет нового пользователя
    @PatchMapping ("/{id}")
    public User update(@PathVariable long id, @Valid @RequestBody UserDto userDto) throws RuntimeException {
        if (userService.getUserStorage().getUserById(id)==null){
            throw new NotFoundException("Пользователь с таким Id не найден");
        }
        User user = userMapper.fromUserDto(userDto);
        user.setId(id);
        return userService.getUserStorage().update(user);
    }

    // Метод удаляющий пользователя
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) throws RuntimeException {
        userService.getUserStorage().delete(id);
    }

    // Метод по получению всех пользователей
    @GetMapping
    public List<User> getAll() throws RuntimeException {
        return userService.getUserStorage().getUsers();
    }

    // Метод по получению одного пользователя (переменная пути)
    @GetMapping("/{id}")
    public User getOne(@PathVariable long id) throws RuntimeException {
        return userService.getUserStorage().getUserById(id);
    }



}
