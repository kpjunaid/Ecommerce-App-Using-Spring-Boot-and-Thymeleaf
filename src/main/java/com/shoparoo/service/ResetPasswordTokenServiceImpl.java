package com.shoparoo.service;

import com.shoparoo.entity.ResetPasswordToken;
import com.shoparoo.entity.User;
import com.shoparoo.exception.TokenNotFoundException;
import com.shoparoo.repository.ResetPasswordTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordTokenServiceImpl implements ResetPasswordTokenService {
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    public ResetPasswordTokenServiceImpl(ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    @Override
    public ResetPasswordToken getById(Long id) {
        return resetPasswordTokenRepository.findById(id).orElseThrow(TokenNotFoundException::new);
    }

    @Override
    public ResetPasswordToken getByToken(String token) {
        return resetPasswordTokenRepository
                .findResetPasswordTokenByToken(token).orElseThrow(TokenNotFoundException::new);
    }

    @Override
    public void saveToken(ResetPasswordToken verificationToken) {
        resetPasswordTokenRepository.save(verificationToken);
    }

    @Override
    public void deleteById(Long id) {
        resetPasswordTokenRepository.deleteById(id);
    }

    @Override
    public void deleteAllByUser(User user) {
        resetPasswordTokenRepository.deleteResetPasswordTokensByUser(user);
    }
}
