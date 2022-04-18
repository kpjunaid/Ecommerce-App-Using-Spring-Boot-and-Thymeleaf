package com.shoparoo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "address")
public class Address extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128)
    private String addressLineOne;

    @Column(length = 128)
    private String addressLineTwo;

    @Column(length = 32)
    private String city;

    @Column(length = 32)
    private String state;

    @Column(length = 32)
    private String zipCode;

    @OneToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToOne(mappedBy = "address")
    private User user;

    public Address(Long id) {
        this.id = id;
    }
}