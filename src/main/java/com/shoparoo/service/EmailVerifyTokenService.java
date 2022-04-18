package com.shoparoo.service;

import com.shoparoo.entity.EmailVerifyToken;
import com.shoparoo.entity.User;

public interface EmailVerifyTokenService {
    EmailVerifyToken getById(Long id);
    EmailVerifyToken getByToken(String token);
    void saveToken(EmailVerifyToken emailVerifyToken);
    void deleteById(Long id);
    void deleteAllByUser(User user);
}
