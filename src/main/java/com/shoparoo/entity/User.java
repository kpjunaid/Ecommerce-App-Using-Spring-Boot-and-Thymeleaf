package com.shoparoo.entity;

import com.shoparoo.util.LocalDateConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false, unique = true)
    private String email;

    @Column(length = 256, nullable = false)
    private String password;

    @Column(length = 32)
    private String firstName;

    @Column(length = 32)
    private String lastName;

    @Column(length = 6)
    private String gender;

    @Column(length = 256)
    private String photoUrl;

    @Column
    @Convert(converter = LocalDateConverter.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private Boolean accountVerified;

    @Column(nullable = false)
    private Boolean emailVerified;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<EmailVerifyToken> emailVerifyTokens = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<ResetPasswordToken> resetPasswordTokens = new HashSet<>();

    public User(String email, String password, Boolean enabled, Boolean accountVerified, Boolean emailVerified) {
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.accountVerified = accountVerified;
        this.emailVerified = emailVerified;
    }

    public User(Long id, String email, String password, Boolean enabled, Boolean accountVerified, Boolean emailVerified) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.accountVerified = accountVerified;
        this.emailVerified = emailVerified;
    }
}