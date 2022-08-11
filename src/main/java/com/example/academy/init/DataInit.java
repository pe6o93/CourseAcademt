package com.example.academy.init;

import com.example.academy.service.CourseService;
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

    @Override
    public void run(String... args) throws Exception {
        this.roleService.initRoles();
        this.courseService.initCourses();
        this.userService.initUsers();
    }
}
