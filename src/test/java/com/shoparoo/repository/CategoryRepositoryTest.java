package com.shoparoo.repository;

import com.shoparoo.entity.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        Category categoryA = new Category(1L, "Category A", true);
        categoryA.setCreatedAt(LocalDateTime.now());
        categoryA.setLastModifiedAt(LocalDateTime.now());

        Category categoryA1 = new Category(2L, "Category A1", true);
        categoryA1.setCreatedAt(LocalDateTime.now());
        categoryA1.setLastModifiedAt(LocalDateTime.now());
        categoryA1.setParentCategory(categoryA);

        Category categoryA2 = new Category(  3L, "Category A2", true);
        categoryA2.setCreatedAt(LocalDateTime.now());
        categoryA2.setLastModifiedAt(LocalDateTime.now());
        categoryA2.setParentCategory(categoryA);

        categoryRepository.save(categoryA);
        categoryRepository.save(categoryA1);
        categoryRepository.save(categoryA2);
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    void shouldReturnOptionalOfCategory_whenValidNameIsGiven() {
        String name = "Category A";
        Optional<Category> returnedCategory = categoryRepository.findByName(name);

        assertThat(returnedCategory.isPresent()).isTrue();
    }

    @Test
    void shouldReturnListOfCategories_whenParentCategoryIsNull() {
        List<Category> categories = categoryRepository.findCategoriesByParentCategoryIsNull();

        assertThat(categories.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnListOfCategories_whenSubCategoryIsNull() {
        List<Category> categories = categoryRepository.findCategoriesBySubCategoriesIsNull();

        assertThat(categories.size()).isEqualTo(2);
    }

    @Test
    void shouldReturnListOfOneSingleCategory_whenValidNameIsGiven() {
        String name = "Category A";
        List<Category> categories = categoryRepository.findCategoriesByName(name);

        assertThat(categories.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnPageOfCategories_whenParentCategoryIsNull() {
        Page<Category> foundPage = categoryRepository.findCategoriesByParentCategoryIsNull(Pageable.ofSize(10));

        assertThat(foundPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldReturnPageOfSubCategories_whenParentCategoryIdIsGiven() {
        Page<Category> foundPage = categoryRepository.findSubCategories(1L, Pageable.ofSize(10));

        assertThat(foundPage.getTotalElements()).isEqualTo(2);
    }
}