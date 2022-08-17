package com.example.academy.web;

import com.example.academy.model.dto.CourseDTO;
import com.example.academy.model.dto.UserDTO;
import com.example.academy.service.CourseService;
import com.example.academy.service.UserService;
import com.example.academy.web.exeption.ObjectNotFoundException;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;


    @GetMapping("/add-course")
    public String addCourse() {
        return "add-course";
    }

    @PostMapping("/add-course")
    public String addBook(@Valid CourseDTO courseDTO,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Principal user) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("courseDTO", courseDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.courseDTO", bindingResult);
            return "redirect:add-course";
        }
        CourseDTO courseDTOWithId = this.courseService.addCourse(courseDTO, user.getName());
        return "redirect:/course/" + courseDTOWithId.getId();
    }

    @GetMapping("/courses")
    public String getCourses(Model model, @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);
        Page<CourseDTO> coursePage = this.courseService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("coursePage", coursePage);
        int totalPages = coursePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "courses";
    }
    @Transactional
    @GetMapping("/course/{id}")
    public String course(@PathVariable Integer id, Model model, Principal user) {
        if (!this.courseService.checkIfCourseExist(id)) {
            throw new ObjectNotFoundException("Курсът не е намерен.");
        }
        UserDTO author = this.userService.findUserByCourseId(id);
        model.addAttribute("course", this.courseService.findById(id));
        model.addAttribute("author", author);
        model.addAttribute("lessons",this.userService.findLessonsByCourse(id));

        if (user != null) {
            this.userService.checkIfUserHaveThisCourse(id, user.getName());
            model.addAttribute("checkCourse", this.userService.checkIfUserHaveThisCourse(id, user.getName()));
            model.addAttribute("checkForAuthor",this.userService.checkIfUserIsAuthor(author,user.getName()));
            if (this.userService.findByUsername(user.getName()).getPoints().compareTo(this.courseService.findById(id).getPoints()) > 0) {
                model.addAttribute("checkForPoints", true);
            }
        }
        return "course";
    }

    @Transactional
    @PostMapping("/course/{id}")
    public String buyCourse(@PathVariable Integer id, Principal user) {
        UserDTO userDTO = this.userService.findByUsername(user.getName());
        CourseDTO courseDTO = this.courseService.findById(id);
        if (userDTO.getPoints().compareTo(courseDTO.getPoints())>0) {
            this.userService.addUserAddCourse(userDTO.getId(),courseDTO.getId());
        }
        return "redirect:/course/"+courseDTO.getId();
    }

    @PreAuthorize("@securityService.hasCourse(#id)")
    @GetMapping("/edit-course/{id}")
    public String getEditCourse(@PathVariable Integer id, Model model){
        CourseDTO course=this.courseService.findById(id);
        model.addAttribute("course", course);
        return "edit-course";
    }

    @PreAuthorize("@securityService.hasCourse(#id)")
    @PatchMapping("/edit-course/{id}")
    public String editCourse(@PathVariable Integer id, @Valid CourseDTO course, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("courseDTO", course);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.courseDTO", bindingResult);
            return "redirect:/edit-course/" + id;
        }
        this.courseService.update(course);

        return "redirect:/course/"+id;
    }
    @Transactional
    @PreAuthorize("@securityService.hasCourse(#id)")
    @DeleteMapping("/course/{id}")
    public String deleteCourse(@PathVariable Integer id){
        this.courseService.deleteCourse(id);
        return "redirect:/profile";
    }

    @ModelAttribute
    public CourseDTO courseDTO() {
        return new CourseDTO();
    }

}
