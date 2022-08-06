package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }


    @Override
    public UserDto create(UserDto userDto) {
        System.out.println("Создаем пользователя");
        if (userDto.getEmail() == null) {
            throw new ValidationException("нельзя чтобы был пустой email");
        }
        User user = userMapper.fromUserDto(userDto);
        User userToReturn = userStorage.create(user);
        return UserMapper.toUserDto(userToReturn);
    }

    @Override
    public void checkId(Long id) {
        userStorage.getUserById(id);
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundException("Пользователь с таким Id не найден");
        }
        User user = userMapper.fromUserDto(userDto);
        user.setId(id);
        User userToReturn = userStorage.update(user);
        return UserMapper.toUserDto(userToReturn);
    }

    @Override
    public void delete(long id) {
        userStorage.delete(id);

    }

    @Override
    public List<UserDto> getUsers() {
        return   userStorage.getUsers()
                .stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

}
