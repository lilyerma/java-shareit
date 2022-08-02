package ru.practicum.shareit.user;


import java.util.List;

public interface UserStorage {


    // Метод по созданию/добавлению нового пользователя
    User create(User user);

    User update(User user)  throws RuntimeException; // Метод обновляющий пользователя или если такого пользователя

    // нет, создает нового (Модификация)
    void delete(Long id) throws RuntimeException; // Метод удаляющий пользователя

    List<User> getUsers() throws RuntimeException; // Метод по возвращению всех пользователей

    User getUserById(long id) throws RuntimeException; // Метод возвращающий пользователя по ID

    long getUserByEmail(String email);
}
