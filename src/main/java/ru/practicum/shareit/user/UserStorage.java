package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserStorage {
 //   User create(User user) throws RuntimeException; // Метод создающий/Добавляющий пользователя в хранилище

    // Метод по созданию/добавлению нового пользователя
    User create(User user);

    User update(User user)  throws RuntimeException; // Метод обновляющий пользователя или если такого пользователя

    // нет, создает нового (Модификация)
    void delete(Long id) throws RuntimeException; // Метод удаляющий пользователя

    List<User> getUsers() throws RuntimeException; // Метод по возвращению всех пользователей

    User getUserById(long id) throws RuntimeException; // Метод возвращающий пользователя по ID

    long getUserByEmail(String email);
}
