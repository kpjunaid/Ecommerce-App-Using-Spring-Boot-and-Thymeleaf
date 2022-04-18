package com.shoparoo.service;

import com.shoparoo.entity.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAllCountries();
    Country getCountryById(Long id);
    Country getCountryByName(String name);
}
