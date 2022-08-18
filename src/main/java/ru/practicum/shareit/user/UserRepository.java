package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(path = "people")
@Repository
public interface UserRepository extends JpaRepository<User,Long> {



    User getUserById(long id) throws RuntimeException; // Метод возвращающий пользователя по ID

}
