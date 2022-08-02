package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Slf4j
@Service
public class UserService {
private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void checkId(Long id) {
        userStorage.getUserById(id);
    }
}
