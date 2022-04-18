package com.shoparoo.repository;

import com.shoparoo.entity.Brand;
import com.shoparoo.entity.Category;
import com.shoparoo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findProductsByCategory(Category category, Pageable pageable);
    Page<Product> findProductsByBrand(Brand brand, Pageable pageable);
    Page<Product> findProductsByNameContainsIgnoreCase(String name, Pageable pageable);
}
