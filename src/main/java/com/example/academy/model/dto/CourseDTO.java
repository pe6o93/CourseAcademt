package com.example.academy.model.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CourseDTO {

    private Integer id;
    @Size(min=5,message = "Заглавието трябва да бъде повече от 5 символа.")
    private String title;
    private String picture;
    private String description;
    private String video;
    @NotNull(message = "Цената трябва да бъде положително число.")
    private BigDecimal points;
    private String created;
    private String authorUsername;
}
