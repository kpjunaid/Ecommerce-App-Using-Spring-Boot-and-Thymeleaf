package com.shoparoo.dto;

import com.shoparoo.annotation.PasswordRepeatEqual;
import com.shoparoo.annotation.ValidEmail;
import com.shoparoo.annotation.ValidPassword;
import com.shoparoo.entity.Address;
import com.shoparoo.entity.Role;
import com.shoparoo.group.UserEmailGroup;
import com.shoparoo.group.UserPasswordGroup;
import com.shoparoo.group.UserSignupGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@PasswordRepeatEqual(
        passwordFieldFirst = "password",
        passwordFieldSecond = "passwordRepeat",
        message = "{user.passwordRepeat.invalid}",
        groups = {UserSignupGroup.class, UserPasswordGroup.class}
)
public class UserDto {
    private Long id;

    @ValidEmail(message = "{user.email.invalid}", groups = {UserSignupGroup.class, UserEmailGroup.class})
    private String email;

    @ValidPassword(message = "{user.password.invalid}",
            groups = {UserSignupGroup.class, UserPasswordGroup.class, UserPasswordGroup.class})
    private String password;

    @NotEmpty(message = "{user.passwordRepeat.invalid}", groups = {UserSignupGroup.class, UserPasswordGroup.class})
    private String passwordRepeat;

    @Size(max = 32, message = "{user.firstName.invalid}")
    private String firstName;

    @Size(max = 32, message = "{user.lastName.invalid}")
    private String lastName;

    @Size(max = 6, message = "{user.gender.invalid}")
    private String gender;

    private Boolean enabled;

    private Boolean accountVerified;

    private Boolean emailVerified;

    @Size(max = 256, message = "{user.photoUrl.invalid}")
    private String photoUrl;

    @Past(message = "{user.birthDate.invalid}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private Set<Role> roles = new HashSet<>();

    private Address address;

    public UserDto(String email, String password, String passwordRepeat, Boolean enabled, Boolean accountVerified, Boolean emailVerified) {
        this.email = email;
        this.password = password;
        this.passwordRepeat = passwordRepeat;
        this.enabled = enabled;
        this.accountVerified = accountVerified;
        this.emailVerified = emailVerified;
    }
}
