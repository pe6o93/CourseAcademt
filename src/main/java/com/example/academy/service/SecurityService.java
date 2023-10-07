package com.example.academy.service;

import com.example.academy.model.dto.LessonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.academy.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;


@Service("securityService")
public class SecurityService {

    @Autowired
    UserService userService;

    @Autowired
    LessonService lessonService;

    Authentication authentication;

    public boolean hasCourse(Integer id) {

        UserDTO user = this.userService.findUserByCourseId(id);
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName().equals(user.getUsername());
    }

    public boolean hasLesson(Integer lessonId) {

        LessonDTO lessonDTO = this.lessonService.findById(lessonId);
        Integer courseId = lessonDTO.getCourse().getId();
        UserDTO user = this.userService.findUserByCourseId(courseId);
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName().equals(user.getUsername());
    }
}