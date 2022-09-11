package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {


    Map<Long, User> mapWithAllUsers = new HashMap<>();
    Long id = 0L;

    // Метод по созданию/добавлению нового пользователя
    @Override
    public User create(User user) {
        if (getUserByEmail(user.getEmail()) != -1) {
            log.debug("Найден пользователь с таким email");
            throw new ConflictException("Такой пользователь уже есть");
        }
        log.debug("Устанавливаем автоматически ID для пользователя");
        user.setId(id + 1);
        id += 1;

        try {
            if (user.getName().isEmpty()) {
                log.debug("Имя было пустое и в качестве имени мы взяли email");
                user.setName(user.getEmail());
            }
            log.debug("Новый пользователь успешно создан/добавлен");
            mapWithAllUsers.put(user.getId(), user);
            return mapWithAllUsers.get(user.getId());
        } catch (RuntimeException e) {
            log.debug("При попытке создать нового пользователя произошла внутренняя ошибка сервера");
            throw new RuntimeException("Внутреняя ошибка сервера");
        }
    }

    // Метод, который обновляет информацию по существующему пользователю
    @Override
    public User update(User user) {
        if (user == null) {
            log.debug("При обновлении пользователя передали значение Null");
            throw new ValidationException("Ошибка валидации");
        }
        if (getUserByEmail(user.email) != -1) {
            throw new ConflictException("Пользователь с таким email уже есть");
        }
        if (mapWithAllUsers.get(user.getId()) == null) {
            log.debug("При обновлении пользователя объект с ID - " + user.getId() + " не был найден");
            throw new NotFoundException("Искомый объект не найден");
        } else {
            try {
                User userExist = mapWithAllUsers.get(user.getId());
                log.debug("Обновляем информацию по пользователю через ID");
                if (user.getName() == null) {
                    user.setName(userExist.getName());
                } else if (user.getEmail() == null) {
                    user.setEmail(userExist.email);
                }
                mapWithAllUsers.put(user.getId(), user);
                return mapWithAllUsers.get(user.getId());
            } catch (RuntimeException e) {
                log.debug("При обновлении пользователя возникла внутренняя ошибка сервера");
                throw new RuntimeException("Внутреняя ошибка сервера");
            }
        }
    }

    // Метод, который удаляет пользователя
    @Override
    public void delete(Long id) {
        if (id == null) {
            log.debug("При удалении пользователя возникла ошибка с NULL");
            throw new NotFoundException("Искомый объект не найден");
        }
        if (id < 0 || mapWithAllUsers.get(id) == null) {
            log.debug("При удалении пользователя возникла ошибка с ID");
            throw new ValidationException("Ошибка валидации");
        } else if (mapWithAllUsers.containsKey(id)) {
            try {
                log.debug("Пытаемся удалить пользователя");
                mapWithAllUsers.remove(id);
            } catch (RuntimeException e) {
                log.debug("При удалении пользователя возникла внутренняя ошибка сервера");
                throw new RuntimeException("Внутреняя ошибка сервера");
            }
        }
    }

    // Метод по возвращению всех пользователей
    @Override
    public List<User> getUsers() {
        try {
            log.debug("Пытаемся вернуть список всех пользователей");
            return new ArrayList<>(mapWithAllUsers.values());
        } catch (RuntimeException exception) {
            log.debug("При попытке вернуть список со всеми пользователями возникла внутренняя ошибка сервера");
            throw new RuntimeException("Внутреняя ошибка сервера");
        }
    }

    // Метод по возвращению пользователя по email
    @Override
    public long getUserByEmail(String email) {
        for (long id : mapWithAllUsers.keySet()) {
            if (mapWithAllUsers.get(id).getEmail().equals(email)) {
                try {
                    log.debug("Пытаюсь вернуть id пользователя");
                    return id;
                } catch (RuntimeException e) {
                    log.debug("При попытке вернуть пользователя возникла внутренняя ошибка сервера");
                    throw new RuntimeException("Внутреняя ошибка сервера");
                }
            }
        }
        log.debug("Пользователь с email не найден");
        return -1;
    }



    // Метод возвращающий пользователя одного по ID
    @Override
    public User getUserById(long id) {
        if (id < 0) {
            log.debug("При попытке вернуть пользователя возникла ошибка с ID");
            throw new NotFoundException("Искомый объект не найден");
        }
        if (mapWithAllUsers.get(id) == null) {
            log.debug("При получения пользователя возникла ошибка с NULL");
            throw new NotFoundException("Поьзователь не найден");
        } else {
            try {
                log.debug("Пытаюсь вернуть одного пользователя");
                return mapWithAllUsers.get(id);
            } catch (RuntimeException e) {
                log.debug("При попытке вернуть пользователя возникла внутренняя ошибка сервера");
                throw new RuntimeException("Внутреняя ошибка сервера");
            }
        }
    }

}
