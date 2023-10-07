package com.example.academy.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "courses")
public class CourseEntity extends BaseEntity{

    @Size(max = 60)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String video;
    private BigDecimal points=BigDecimal.ZERO;
    private LocalDateTime created;

    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity author;

    @ManyToOne
    private PictureEntity picture;

   // @LazyCollection(LazyCollectionOption.FALSE)
//    @OneToMany(fetch = FetchType.LAZY)
//    private List<LessonEntity> lessons;

    @PrePersist
    public void create() {
        this.created = LocalDateTime.now();
    }

}
