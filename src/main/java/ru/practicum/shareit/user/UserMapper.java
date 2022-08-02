package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Objects;

@Component
public class UserMapper {

    private final UserStorage userStorage;

    @Autowired
    public UserMapper(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getName(),
                user.getEmail()
        );
    }

    public User fromUserDto(UserDto userDto) {
        return new User(
                0L,
                userDto.getName(),
                userDto.getEmail()
        );
    }


    private Long getUserId(String email) {
        return userStorage.getUserByEmail(email);
    }


}
