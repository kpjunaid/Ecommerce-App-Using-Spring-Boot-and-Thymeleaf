package com.shoparoo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CountryDto {
    private Long id;

    @Size(max = 64, message = "{country.name.invalid}")
    private String name;

    @Size(max = 128, message = "{country.officialName.invalid}")
    private String officialName;

    @Size(max = 64, message = "{country.capital.invalid}")
    private String capital;

    @Size(max = 32, message = "{country.callingCode.invalid}")
    private String callingCode;

    @Size(max = 32, message = "{country.countryCode.invalid}")
    private String countryCode;

    @Size(max = 32, message = "{country.currencyName.invalid}")
    private String currencyName;

    @Size(max = 3, message = "{country.currencyCode.invalid}")
    private String currencyCode;

    @Size(max = 1, message = "{country.currencySymbol.invalid}")
    private String currencySymbol;

    @Size(max = 256, message = "{country.flagUrl.invalid}")
    private String flagUrl;
}
