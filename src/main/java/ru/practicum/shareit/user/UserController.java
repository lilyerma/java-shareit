package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public UserController(UserService userService, ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }


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
        itemService.deleteByOwner(id);
    }

    // Метод по получению всех пользователей
    @GetMapping
    public List<UserDto> getUsers() throws RuntimeException {
        return userService.getUsers();
    }

    // Метод по получению одного пользователя (переменная пути)
    @GetMapping("/{id}")
    public User getOne(@PathVariable long id) throws RuntimeException {
        return userService.getUserStorage().getUserById(id);
    }



}
