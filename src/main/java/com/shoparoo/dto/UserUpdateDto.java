package com.shoparoo.dto;

import com.shoparoo.entity.Country;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDto {
    @Size(max = 32, message = "{user.firstName.invalid}")
    private String firstName;

    @Size(max = 32, message = "{user.lastName.invalid}")
    private String lastName;

    @Size(max = 6, message = "{user.gender.invalid}")
    private String gender;

    @Size(max = 256, message = "{user.phone.invalid}")
    private String phone;

    @Size(max = 256, message = "{user.photoUrl.invalid}")
    private String photoUrl;

    @Past(message = "{user.birthDate.invalid}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Size(max = 128, message = "{user.addressLineOne.invalid}")
    private String addressLineOne;

    @Size(max = 128, message = "{user.addressLineTwo.invalid}")
    private String addressLineTwo;

    @Size(max = 32, message = "{user.city.invalid}")
    private String city;

    @Size(max = 32, message = "{user.state.invalid}")
    private String state;

    @Size(max = 32, message = "{user.zipCode.invalid}")
    private String zipCode;

    private Country country;
}
