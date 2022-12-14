package com.example.academy.model.dto;

import com.example.academy.model.entity.CourseEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class LessonDTO {

    private Integer id;
    @NotNull(message = "Заглавието не може да бъде празно.")
    @Length(min = 5, message = "Заглавието трябва да бъде с повече от 5 символа.")
    private String title;
    private String description;
    private String created;
    private CourseDTO course;

}
