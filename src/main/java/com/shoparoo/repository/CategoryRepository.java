package com.shoparoo.repository;

import com.shoparoo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findCategoriesByParentCategoryIsNull();
    List<Category> findCategoriesBySubCategoriesIsNull();
    List<Category> findCategoriesByName(String name);
    Page<Category> findCategoriesByParentCategoryIsNull(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.parentCategory.id = ?1")
    Page<Category> findSubCategories(Long parentId, Pageable pageable);
}
