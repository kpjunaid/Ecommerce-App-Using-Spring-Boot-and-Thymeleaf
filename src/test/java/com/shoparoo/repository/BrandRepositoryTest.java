package com.shoparoo.repository;

import com.shoparoo.entity.Brand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BrandRepositoryTest {
    @Autowired
    BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        Brand brandA = new Brand(1L, "Brand A", true);
        brandA.setCreatedAt(LocalDateTime.now());
        brandA.setLastModifiedAt(LocalDateTime.now());

        brandRepository.save(brandA);
    }

    @AfterEach
    void tearDown() {
        brandRepository.deleteAll();
    }

    @Test
    void shouldReturnOptionalOfBrand_whenNameIsGiven() {
        String name = "Brand A";
        Optional<Brand> returnedBrand = brandRepository.findByName(name);

        assertThat(returnedBrand.isPresent()).isTrue();
    }

    @Test
    void shouldReturnListOfOneSingleBrand_whenNameIsGiven() {
        String name = "Brand A";
        List<Brand> returnedBrand = brandRepository.findBrandsByName(name);

        assertThat(returnedBrand.size()).isEqualTo(1);
    }
}