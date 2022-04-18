package com.shoparoo.repository;

import com.shoparoo.entity.ResetPasswordToken;
import com.shoparoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    Optional<ResetPasswordToken> findResetPasswordTokenByToken(String token);
    void deleteResetPasswordTokensByUser(User user);
}
