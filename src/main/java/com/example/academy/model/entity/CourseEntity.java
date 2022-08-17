package com.example.academy.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.cglib.core.Local;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "courses")
public class CourseEntity extends BaseEntity{

    @Size(max = 60)
    private String title;
    private String picture;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String video;
    private BigDecimal points=BigDecimal.ZERO;
    private LocalDateTime created;

    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity author;

   // @LazyCollection(LazyCollectionOption.FALSE)
//    @OneToMany(fetch = FetchType.LAZY)
//    private List<LessonEntity> lessons;

    @PrePersist
    public void create() {
        this.created = LocalDateTime.now();
    }
}
