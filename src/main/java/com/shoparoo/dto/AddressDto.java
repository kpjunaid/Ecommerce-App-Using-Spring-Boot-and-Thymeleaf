package com.shoparoo.dto;

import com.shoparoo.entity.Country;
import com.shoparoo.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class AddressDto {
    private Long id;

    @Size(max = 128)
    private String addressLineOne;

    @Size(max = 128)
    private String addressLineTwo;

    @Size(max = 32)
    private String city;

    @Size(max = 32)
    private String state;

    @Size(max = 32)
    private String zipCode;

    private Country country;

    private User user;
}
