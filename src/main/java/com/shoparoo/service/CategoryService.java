package com.shoparoo.service;

import com.shoparoo.dto.CategoryDto;
import com.shoparoo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    List<Category> getAllRootCategories();
    List<Category> getAllLeafCategories();
    List<CategoryDto> getAllHierarchicalCategories();
    List<Category> getAllUpperCategoriesById(Long id);
    Page<Category> getAllRootCategoriesPageable(Integer pageNumber, Integer contentPerPage);
    Page<Category> getAllSubCategoriesPageable(Long parentId, Integer pageNumber, Integer contentPerPage);
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    void createNewCategory(CategoryDto categoryDto, MultipartFile categoryPhoto);
    void updateCategory(Long id, CategoryDto categoryDto, MultipartFile categoryPhoto);
    void deleteCategoryById(Long id);
}
