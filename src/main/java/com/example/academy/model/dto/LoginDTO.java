package com.example.academy.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class LoginDTO {

    @NotEmpty
    @Size(min = 6,max = 20,message = "Потребителското име е невалидно")
    private String username;

    @Size(min = 4,max = 20,message = "Паролата е невалидна")
    private String password;
}
