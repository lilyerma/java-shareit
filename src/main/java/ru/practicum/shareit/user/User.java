package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Data
public class User {

    Long id;
    @NotNull
    String name;
    @Email
    @NotNull(message = "Не должно быть null")
    @NotBlank(message = "Не должно быть пустым")
    String email;
    public User(Long userId, String name, String email) {
        this.name = name;
        this.email = email;
    }

}
