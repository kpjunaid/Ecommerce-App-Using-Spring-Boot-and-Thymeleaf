package com.shoparoo.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void send(String to, String subject, String mail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");
            messageHelper.setText(mail, true);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setFrom("noreply@shoparoo.com");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email");
        }
    }

    @Override
    public String buildAccountConfirmationMail(String token) {
        String url = "http://localhost:8080/verify-email?token=" + token;
        return "<div>" +
                    "<h3>Welcome to Shoparoo!</h3>" +
                    "<p>Your account was successfully created. Please, click on the link bellow to verify your account.</p>" +
                    "<a href=\" " + url + " \">Confirm Account</a>" +
                "</div>";
    }

    @Override
    public String buildResetPasswordMail(String token) {
        String url = "http://localhost:8080/reset-password?token=" + token;
        return "<div>" +
                "<h3>You Forgot Password</h3>" +
                    "<p>Please, click on the link bellow to reset your password.</p>" +
                    "<a href=\" " + url + " \">Get New Password</a>" +
                "</div>";
    }
}
