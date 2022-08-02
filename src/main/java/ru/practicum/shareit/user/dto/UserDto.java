package ru.practicum.shareit.user.dto;

import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data

public class UserDto {


    private String name;
    @Email
    private String email;


    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }


}
