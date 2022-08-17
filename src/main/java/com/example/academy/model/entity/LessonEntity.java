package com.example.academy.model.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "lessons")
public class LessonEntity extends BaseEntity {

    @Length(min = 5, max = 50,message = "Заглавието трябва да бъде с повече от 5 символа.")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;
    @PrePersist
    public void create() {
        this.created = LocalDateTime.now();
    }
}
