package com.example.academy.model.dto;

import com.example.academy.model.enums.GenderEnum;
import com.example.academy.model.enums.RolesEnum;
import com.example.academy.model.validator.UniqueEmail;
import com.example.academy.model.validator.UniqueUserName;
import lombok.Data;


import javax.validation.constraints.*;

@Data
public class RegisterDto {

    @UniqueUserName
    @Size(min = 6,max = 20,message = "Потребителското име трябва да съдържа повече от 5 символа.")
    private String username;


    @Size(min = 3,max = 20,message = "Името трябва да съдържа повече от 2 символа.")
    private String firstName;


    @Size(min = 3,max = 20,message = "Фамилията трябва да съдържа повече от 2 символа.")
    private String lastName;


    @UniqueEmail
    @Email(message = "Емейл адресът трябва да бъде валиден.")
    private String email;

    @Min(value = 12,message = "Минималната възраст е 12 години.")
    private int age;


    @Size(min = 4,max = 20,message = "Паролата трябва да бъде повече от 3 символа.")
    private String password;


    @Size(min = 4,max = 20,message = "Паролата трябва да бъде повече от 3 символа.")
    private String confirmPassword;

    private RolesEnum role;

    @NotNull(message = "Моля изберете пол.")
    private GenderEnum gender;
}
