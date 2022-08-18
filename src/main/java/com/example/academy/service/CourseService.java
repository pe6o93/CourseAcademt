package com.example.academy.service;

import com.example.academy.model.dto.CourseDTO;
import com.example.academy.model.dto.LessonDTO;
import com.example.academy.model.entity.CourseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CourseService {

    List<CourseDTO> mapCoursesToDTO(List<CourseEntity> courses);

    void initCourses();

    CourseDTO addCourse(CourseDTO courseDTO, String username);

    boolean checkIfCourseExist(Integer id);

    CourseDTO findById(Integer id);

    List<CourseDTO> getLastCoursesToDTO();

    Page<CourseDTO> findPaginated(PageRequest pageable);

    void update(CourseDTO courseDTO);

    void deleteCourse(Integer courseId);

    void addLessonToCourse(LessonDTO lessonDTO, Integer courseId);

//    CourseDTO findByLessonId(Integer id);
}
