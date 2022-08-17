package com.example.academy.web;

import com.example.academy.model.entity.EmailDetails;
import com.example.academy.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/sendMail")
    public String
    sendMail(@RequestParam String name) {
        EmailDetails emailDetails=new EmailDetails();
        emailDetails.setRecipient("courseacademysender@gmail.com");
        emailDetails.setMsgBody("dsasadsdasdaasddasasdasd");
        emailDetails.setSubject("otnosno tova");
        String status
                = emailService.sendSimpleMail(emailDetails);

        return "index";
    }

    @ModelAttribute
    public EmailDetails emailDetails() {
        return new EmailDetails();
    }
}
