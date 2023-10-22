package com.example.academy.web;


import com.example.academy.model.dto.CourseDTO;
import com.example.academy.model.entity.EmailDetails;
import com.example.academy.service.CourseService;
import com.example.academy.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@AllArgsConstructor
public class HomeController {

    private final CourseService courseService;
    private final UserService userService;

    @Transactional
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("lastCourses", this.courseService.getLastCoursesToDTO());
        model.addAttribute("lastTeachers", this.userService.getLastTeachersToDTO());
        model.addAttribute(EmailDetails.ENTITY_NAME, new EmailDetails());
        return "index";
    }

}
