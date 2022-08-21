package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


//
//    User getUserById(long id) throws RuntimeException; // Метод возвращающий пользователя по ID

}
