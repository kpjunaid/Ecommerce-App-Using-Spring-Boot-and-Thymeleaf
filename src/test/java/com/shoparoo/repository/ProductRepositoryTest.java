package com.shoparoo.repository;

import com.shoparoo.entity.Brand;
import com.shoparoo.entity.Category;
import com.shoparoo.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        Category categoryA = new Category("Category A", true);
        categoryA.setCreatedAt(LocalDateTime.now());
        categoryA.setLastModifiedAt(LocalDateTime.now());
        categoryRepository.save(categoryA);

        Brand brandA = new Brand("Brand A", true);
        brandA.setCreatedAt(LocalDateTime.now());
        brandA.setLastModifiedAt(LocalDateTime.now());
        brandRepository.save(brandA);

        Product productA = new Product(
                "Product A",
                149.00F,
                199.00F,
                10,
                true,
                0,
                true,
                categoryA,
                brandA
        );
        productA.setCreatedAt(LocalDateTime.now());
        productA.setLastModifiedAt(LocalDateTime.now());
        productRepository.save(productA);

        Product productB = new Product(
                "Product B",
                149.00F,
                199.00F,
                10,
                true,
                0,
                true,
                categoryA,
                brandA
        );
        productB.setCreatedAt(LocalDateTime.now());
        productB.setLastModifiedAt(LocalDateTime.now());
        productRepository.save(productB);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        brandRepository.deleteAll();
    }

    @Test
    void shouldReturnPageOfProductsByCategory_whenCategoryIsGiven() {
        Optional<Category> category = categoryRepository.findByName("Category A");

        Page<Product> foundPage = productRepository.findProductsByCategory(category.get(), Pageable.ofSize(10));

        assertThat(foundPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    void shouldReturnPageOfProductsByBrand_whenBrandIsGiven() {
        Optional<Brand> brand = brandRepository.findByName("Brand A");

        Page<Product> foundPage = productRepository.findProductsByBrand(brand.get(), Pageable.ofSize(10));

        assertThat(foundPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    void shouldReturnPageOfProductsContainingName_whenStringAsNameIsGiven() {
        String searchString = "Prod";

        Page<Product> foundPage = productRepository.findProductsByNameContainsIgnoreCase(searchString, Pageable.ofSize(10));

        assertThat(foundPage.getTotalElements()).isEqualTo(2);
    }
}