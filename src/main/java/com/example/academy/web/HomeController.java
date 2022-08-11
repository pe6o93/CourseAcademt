package com.example.academy.web;


import com.example.academy.model.dto.CourseDTO;
import com.example.academy.service.CourseService;
import com.example.academy.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@AllArgsConstructor
public class HomeController {

    private final CourseService courseService;
    private final UserService userService;

    @GetMapping("/")
    public String home(Model model){
       List<CourseDTO> lastCourses= this.courseService.getLastCoursesToDTO();
       model.addAttribute("lastCourses",lastCourses);
       model.addAttribute("lastTeachers",this.userService.getLastTeachersToDTO());
       return "index";
    }


}
