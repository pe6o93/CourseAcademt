package com.example.academy.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class AddCourseDTO {

    private Integer id;
    @Size(min=5,message = "Заглавието трябва да бъде повече от 5 символа.")
    private String title;
    private MultipartFile picture;
    private String description;
    private String video;
    @NotNull(message = "Цената трябва да бъде положително число.")
    private BigDecimal points;
    private String created;
    private String authorUsername;
}
