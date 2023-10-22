package com.example.academy.repository;

import com.example.academy.model.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {

    @Query("SELECT c FROM CourseEntity c ")
    List<CourseEntity> getCourseEntitiesByUserEntity(Integer userId);

    @Query(nativeQuery = true, value = "SELECT * FROM courses ORDER BY created DESC LIMIT 3")
    List<CourseEntity> getCoursesOrderedByCreatedLast3();

    @Query("SELECT c.points FROM CourseEntity c WHERE c.id = ?1")
    BigDecimal getCoursePointsById(Integer id);
}
