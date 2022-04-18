package com.shoparoo.service;

public interface EmailService {
    void send(String to, String subject, String mail);
    String buildAccountConfirmationMail(String token);
    String buildResetPasswordMail(String token);
}
