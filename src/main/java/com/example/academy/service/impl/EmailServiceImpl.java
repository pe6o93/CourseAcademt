package com.example.academy.service.impl;

import com.example.academy.model.entity.ContactMailEntity;
import com.example.academy.model.entity.EmailDetails;
import com.example.academy.model.enums.MailStatusEnum;
import com.example.academy.repository.ContactMailRepository;
import com.example.academy.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender javaMailSender;
    private final ContactMailRepository contactMailRepository;

    @Value("${spring.mail.username}")
    private String receiver;

    public EmailServiceImpl(JavaMailSender javaMailSender,
                            ContactMailRepository contactMailRepository) {
        this.javaMailSender = javaMailSender;
        this.contactMailRepository = contactMailRepository;
    }

    @Override
    public String sendSimpleMail(EmailDetails details) {

        MailStatusEnum mailStatusEnum = MailStatusEnum.IN_PROGRESS;

        // Try block to check for exceptions
        try {

            // Creating a simple mail message
            final SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(details.getRecipient());
            mailMessage.setTo(receiver);
            mailMessage.setText(details.getMsgBody());

            String subject = getSubject(details);
            mailMessage.setSubject(subject);

            // Sending the mail
            javaMailSender.send(mailMessage);
            mailStatusEnum = MailStatusEnum.SEND;
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            mailStatusEnum = MailStatusEnum.FAILED;
            return "Error while Sending Mail";
        } finally {
            saveMailInfo(details, mailStatusEnum);
        }
    }

    private void saveMailInfo(EmailDetails details, MailStatusEnum mailStatusEnum) {
        final ContactMailEntity entity = new ContactMailEntity();
        entity.setFullName(details.getName());
        entity.setText(details.getMsgBody());
        entity.setEmail(details.getRecipient());
        entity.setPhone(details.getPhone());
        entity.setStatus(mailStatusEnum);
        contactMailRepository.save(entity);
    }

    @Override
    public String sendMailWithAttachment(EmailDetails details) {
        // Creating a mime message
        MimeMessage mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            // Setting multipart as true for attachments to
            // be send
            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(receiver);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(getSubject(details));


            // Adding the attachment
//            FileSystemResource file
//                    = new FileSystemResource(
//                    new File(details.getAttachment()));
//
//            mimeMessageHelper.addAttachment(
//                    file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }
        // Catch block to handle MessagingException
        catch (MessagingException e) {

            // Display message when exception occurred
            return "Error while sending mail!!!";
        }

    }

    private static String getSubject(EmailDetails details) {
        return "Received email from: " + details.getName() + " with phone number: " + details.getPhone();
    }
}
