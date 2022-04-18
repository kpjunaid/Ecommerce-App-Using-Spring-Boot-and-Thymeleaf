package com.shoparoo.service;

import com.shoparoo.entity.Country;
import com.shoparoo.exception.CountryNotFoundException;
import com.shoparoo.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Country getCountryById(Long id) {
        return countryRepository.findById(id).orElseThrow(
                () -> new CountryNotFoundException("Country not found"));
    }

    @Override
    public Country getCountryByName(String name) {
        return countryRepository.findCountryByName(name).orElseThrow(
                () -> new CountryNotFoundException("Country not found"));
    }
}
