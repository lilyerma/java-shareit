package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    private long id;
    private String name;
    @Email
    @NonNull
    private String email;


    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }


}
