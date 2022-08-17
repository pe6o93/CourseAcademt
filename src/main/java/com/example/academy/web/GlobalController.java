package com.example.academy.web;

import com.example.academy.model.dto.CourseDTO;
import com.example.academy.model.dto.UserDTO;
import com.example.academy.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

@ControllerAdvice
@AllArgsConstructor
public class GlobalController extends ResponseEntityExceptionHandler {

    private final UserService userService;

    @Transactional
    @ModelAttribute("myCourses")
    public List<CourseDTO> myCourses(Principal user){
        if(user!=null) {
           return this.userService.findByUsername(user.getName()).getCourses();
        }
        return null;
    }

    @ModelAttribute("haveAny")
    public Boolean haveCourse(Principal user){
        if(user!=null) {
            return !this.userService.findByUsername(user.getName()).getCourses().isEmpty() ;
        }
        return false;
    }

}