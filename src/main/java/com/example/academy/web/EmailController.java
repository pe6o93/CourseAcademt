package com.example.academy.web;

import com.example.academy.model.entity.EmailDetails;
import com.example.academy.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/sendMail")
    public String
    sendMail(  EmailDetails emailDetails) {
        String status
                = emailService.sendSimpleMail(emailDetails);
        return "index";
    }

}
