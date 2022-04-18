package com.shoparoo.repository;

import com.shoparoo.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByName(String name);
    List<Brand> findBrandsByName(String name);
}
