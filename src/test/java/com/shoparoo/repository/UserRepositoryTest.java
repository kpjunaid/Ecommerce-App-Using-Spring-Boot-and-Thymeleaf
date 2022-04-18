package com.shoparoo.repository;

import com.shoparoo.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User(
                "johndoe@dom.com",
                "@P4ssword",
                true,
                false,
                false
        );
        user.setCreatedAt(LocalDateTime.now());
        user.setLastModifiedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnOptionalOfUser_whenEmailIsGiven() {
        String email = "johndoe@dom.com";
        Optional<User> returnedUser = userRepository.findByEmail(email);

        assertThat(returnedUser.isPresent()).isTrue();
    }
}