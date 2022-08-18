package com.example.academy.init;

import com.example.academy.service.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
