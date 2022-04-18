package com.shoparoo.service;

import com.shoparoo.entity.EmailVerifyToken;
import com.shoparoo.entity.User;
import com.shoparoo.exception.TokenNotFoundException;
import com.shoparoo.repository.EmailVerifyTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class EmailVerifyTokenServiceImpl implements EmailVerifyTokenService {
    private final EmailVerifyTokenRepository emailVerifyTokenRepository;

    public EmailVerifyTokenServiceImpl(EmailVerifyTokenRepository emailVerifyTokenRepository) {
        this.emailVerifyTokenRepository = emailVerifyTokenRepository;
    }

    @Override
    public EmailVerifyToken getById(Long id) {
        return emailVerifyTokenRepository.findById(id).orElseThrow(TokenNotFoundException::new);
    }

    @Override
    public EmailVerifyToken getByToken(String token) {
        return emailVerifyTokenRepository
                .findEmailVerifyTokenByToken(token).orElseThrow(TokenNotFoundException::new);
    }

    @Override
    public void saveToken(EmailVerifyToken emailVerifyToken) {
        emailVerifyTokenRepository.save(emailVerifyToken);
    }

    @Override
    public void deleteById(Long id) {
        emailVerifyTokenRepository.deleteById(id);
    }

    @Override
    public void deleteAllByUser(User user) {
        emailVerifyTokenRepository.deleteEmailVerifyTokensByUser(user);
    }
}
