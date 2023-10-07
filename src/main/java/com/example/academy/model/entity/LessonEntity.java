package com.example.academy.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "lessons")
public class LessonEntity extends BaseEntity {

    @Length(min = 5, max = 50, message = "Заглавието трябва да бъде с повече от 5 символа.")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private CourseEntity course;

    @PrePersist
    public void create() {
        this.created = LocalDateTime.now();
    }


}
