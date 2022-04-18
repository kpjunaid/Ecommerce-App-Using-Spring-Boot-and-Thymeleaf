package com.shoparoo.repository;

import com.shoparoo.entity.EmailVerifyToken;
import com.shoparoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerifyTokenRepository extends JpaRepository<EmailVerifyToken, Long> {
    Optional<EmailVerifyToken> findEmailVerifyTokenByToken(String token);
    void deleteEmailVerifyTokensByUser(User user);
}
