package com.example.academy.service;

import com.example.academy.model.dto.LessonDTO;
import com.example.academy.model.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service("securityService")
public class SecurityService {

    private final UserService userService;

    private final LessonService lessonService;

    private Authentication authentication;

    public SecurityService(UserService userService, LessonService lessonService) {
        this.userService = userService;
        this.lessonService = lessonService;
    }

    public boolean hasCourse(Integer id) {

        UserDTO user = this.userService.findUserByCourseId(id);
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName().equals(user.getUsername());
    }

    public boolean hasLesson(Integer lessonId) {

        LessonDTO lessonDTO = this.lessonService.findById(lessonId);
        Integer courseId = lessonDTO.getCourse().getId();
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return this.userService.checkIfUserHaveThisCourse(courseId, username);
    }
}