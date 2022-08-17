package com.example.academy.repository;

import com.example.academy.model.dto.LessonDTO;
import com.example.academy.model.entity.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity,Integer> {
    List<LessonEntity> findAllByCourseId(Integer courseId);
}
