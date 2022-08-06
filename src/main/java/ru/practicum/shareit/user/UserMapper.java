package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;


@Component
public class UserMapper {

    private final UserStorage userStorage;

    @Autowired
    public UserMapper(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto(
                user.getName(),
                user.getEmail()
        );
        userDto.setId(user.getId());
        return userDto;
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
