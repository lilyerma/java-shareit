package ru.practicum.shareit.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(nullable = false)
    String name;
    @Email
    @NotNull(message = "Не должно быть null")
    @NotBlank(message = "Не должно быть пустым")
    @Column(unique = true,nullable = false)
    String email;

    public User(){};
    public User(Long userId, String name, String email) {
        this.name = name;
        this.email = email;
    }

}
