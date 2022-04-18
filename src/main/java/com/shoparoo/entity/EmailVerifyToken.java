package com.shoparoo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "email_verify_token")
public class EmailVerifyToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 256, nullable = false, updatable = false, unique = true)
    private String token;

    @Column(nullable = false, updatable = false)
    private LocalDateTime expireAt;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public EmailVerifyToken(Long id, String token, LocalDateTime expireAt) {
        this.id = id;
        this.token = token;
        this.expireAt = expireAt;
    }
}
