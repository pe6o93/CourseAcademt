package com.example.academy.init;

import com.example.academy.service.CourseService;
import com.example.academy.service.LessonService;
import com.example.academy.service.PictureService;
import com.example.academy.service.RoleService;
import com.example.academy.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInit implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final CourseService courseService;
    private final LessonService lessonService;
    private final PictureService pictureService;

    @Override
    public void run(String... args) throws Exception {
        this.pictureService.initPictures();
        this.roleService.initRoles();
        this.userService.initUsers();
        this.lessonService.initLessons();
        this.courseService.initCourses();
    }
}
