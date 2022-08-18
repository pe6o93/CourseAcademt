package com.example.academy.web;

import com.example.academy.model.dto.CourseDTO;
import com.example.academy.model.dto.LoginDTO;
import com.example.academy.model.dto.RegisterDto;
import com.example.academy.model.dto.UserDTO;
import com.example.academy.model.entity.UserEntity;
import com.example.academy.service.CourseService;
import com.example.academy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @Transactional
    @PostMapping("/register")
    public String registerConfirm(@Valid RegisterDto registerDto,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors() || !registerDto.getPassword()
                .equals(registerDto.getConfirmPassword())) {

            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerDto", bindingResult);

            return "redirect:register";
        }
        this.userService.registerAndLoginUser(registerDto);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        if (!model.containsAttribute("isFound")) {
            model.addAttribute("isFound", true);
        }
        return "login";
    }

    @PostMapping("/login-error")
    public String failedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
            String userName,
            RedirectAttributes attributes
    ) {

        attributes.addFlashAttribute("bad_credentials", true);
        attributes.addFlashAttribute("username", userName);
        return "redirect:/login";
    }

    @Transactional
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        if (principal!=null) {
            UserDTO userDTO = this.userService.findByUsername(principal.getName());
            model.addAttribute("user", userDTO);
            model.addAttribute("role", this.userService.getBiggestRole(userDTO));
        }

        return "profile";
    }

    @GetMapping("/profile/{id}/edit")
    public String editOffer(@PathVariable Integer id, Model model) {
        UserDTO byIdAndMapToDTO = this.userService.findByIdAndMapToDTO(id);
        model.addAttribute("user", byIdAndMapToDTO);
        return "edit-profile";
    }

    @PatchMapping("/profile/{id}/edit")
    public String editUser(
            @PathVariable Integer id,
            @Valid UserDTO userDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) throws ObjectNotFoundException {
        UserDTO byIdAndMapToDTO = this.userService.findByIdAndMapToDTO(id);
        model.addAttribute("user", byIdAndMapToDTO);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userDTO", userDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userDTO", bindingResult);
            return "redirect:/profile/" + id + "/edit";
        }
        this.userService.update(userDTO);
        return "redirect:/profile";
    }

    @Transactional
    @GetMapping("/profile-user/{id}")
    public String profileId(@PathVariable Integer id, Model model) {

        UserDTO user = this.userService.findByIdAndMapToDTO(id);
        model.addAttribute("user", user);
        model.addAttribute("role", this.userService.getBiggestRole(user));
        return "profile-user";
    }

    @Transactional
    @GetMapping("/teachers")
    public String getTeachers(Model model, @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);
        Page<UserDTO> teachersPage = this.userService.findPaginatedTeachers(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("teachersPage", teachersPage);
        int totalPages = teachersPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "teachers";
    }

    @GetMapping("/add-points")
    public String getPointsView() {
        return "add-points";
    }

    @PostMapping("/points")
    public String addPoints(@RequestParam String points, Principal principal) {
        this.userService.addMorePoints(points, principal.getName());
        return "redirect:profile";
    }

    @ModelAttribute
    public RegisterDto registerDto() {
        return new RegisterDto();
    }

    @ModelAttribute
    public LoginDTO loginDTO() {
        return new LoginDTO();
    }
}
