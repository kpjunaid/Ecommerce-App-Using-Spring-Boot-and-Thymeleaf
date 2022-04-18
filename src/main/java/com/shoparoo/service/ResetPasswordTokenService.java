package com.shoparoo.service;

import com.shoparoo.entity.ResetPasswordToken;
import com.shoparoo.entity.User;

public interface ResetPasswordTokenService {
    ResetPasswordToken getById(Long id);
    ResetPasswordToken getByToken(String token);
    void saveToken(ResetPasswordToken resetPasswordToken);
    void deleteById(Long id);
    void deleteAllByUser(User user);
}
