package com.example.academy.service;

import com.example.academy.model.dto.AddCourseDTO;
import com.example.academy.model.dto.CourseDTO;
import com.example.academy.model.dto.LessonDTO;
import com.example.academy.model.entity.CourseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface CourseService {

    List<CourseDTO> mapCoursesToDTO(List<CourseEntity> courses);

    CourseDTO addCourse(AddCourseDTO addCourseDTO, String username) throws IOException;

    boolean checkIfCourseExist(Integer id);

    CourseDTO findById(Integer id);

    List<CourseDTO> getLastCoursesToDTO();

    Page<CourseDTO> findPaginated(PageRequest pageable);

    void update(CourseDTO courseDTO);

    void deleteCourse(Integer courseId);

    void addLessonToCourse(LessonDTO lessonDTO, Integer courseId);

    BigDecimal getCoursePointById(Integer id);

    void addCourseToUser(Integer courseId, String username);

    List<CourseDTO> getCourseByUsername(String name);
}
