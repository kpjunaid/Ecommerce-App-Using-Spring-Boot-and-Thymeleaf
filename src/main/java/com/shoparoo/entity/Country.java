package com.shoparoo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "country")
public class Country extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String name;

    @Column(length = 128)
    private String officialName;

    @Column(length = 64)
    private String capital;

    @Column(length = 32)
    private String countryCode;

    @Column(length = 32)
    private String callingCode;

    @Column(length = 32)
    private String currencyName;

    @Column(length = 3)
    private String currencyCode;

    @Column(length = 1)
    private String currencySymbol;

    @Column(length = 256)
    private String flagUrl;
}
