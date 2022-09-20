package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

  //  void checkId(Long id);

    void checkId(Long id);

    UserDto update(UserDto userDto, long id);

    void delete(long id);

    List<UserDto> getUsers();

    UserDto getUserById(long id);

  //  UserStorage getUserStorage();

//    UserMapper getUserMapper();
}
