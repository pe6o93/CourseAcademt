package com.example.academy.web;

import com.example.academy.model.dto.CourseDTO;
import com.example.academy.model.dto.LessonDTO;
import com.example.academy.service.CourseService;
import com.example.academy.service.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class LessonController {

    private final CourseService courseService;
    private final LessonService lessonService;

    @GetMapping("/add-lesson")
    public String addLessonView(@RequestParam Integer courseId, Model model) {
        CourseDTO currentCourse = this.courseService.findById(courseId);
        model.addAttribute("currentCourse", currentCourse);
        return "add-lesson";
    }

    @PostMapping("/add-lesson")
    public String addLesson(@Valid LessonDTO lessonDTO, @RequestParam Integer courseId) {
        this.courseService.addLessonToCourse(lessonDTO, courseId);
        return "redirect:/lesson/" + lessonDTO.getId();
    }

    @Transactional
    @PreAuthorize("@securityService.hasLesson(#id)")
    @GetMapping("/lesson/{id}")
    public String getLesson(@PathVariable(value = "id") Integer id, Model model) {
        model.addAttribute("course", this.lessonService.findById(id).getCourse());
        model.addAttribute("lesson", this.lessonService.findById(id));
        return "lesson";
    }

    @PreAuthorize("@securityService.hasLesson(#id)")
    @DeleteMapping("/lesson/{id}")
    public String deleteLesson(@PathVariable Integer id) {
        this.lessonService.deleteById(id);
        return "profile";
    }

    @Transactional
    @PreAuthorize("@securityService.hasLesson(#id)")
    @GetMapping("/edit-lesson/{id}")
    public String editLessonView(@PathVariable Integer id, Model model) {
        model.addAttribute("lesson", this.lessonService.findById(id));
        return "edit-lesson";
    }

    @PatchMapping("/edit-lesson/{id}")
    public String editLesson(@PathVariable Integer id, @Valid LessonDTO lessonDTO, RedirectAttributes redirectAttributes, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("lessonDTO", lessonDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.lessonDTO", bindingResult);
            return "redirect:/edit-lesson/" + id;
        }
        this.lessonService.updateLesson(lessonDTO);
        return "redirect:/lesson/" + id;
    }

    @ModelAttribute
    public LessonDTO lessonDTO() {
        return new LessonDTO();
    }
}
