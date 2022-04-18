package com.shoparoo.service;

import com.shoparoo.dto.CategoryDto;
import com.shoparoo.entity.Category;
import com.shoparoo.exception.CategoryNotFoundException;
import com.shoparoo.mapper.MapStructMapper;
import com.shoparoo.mapper.MapStructMapperUpdate;
import com.shoparoo.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
class CategoryServiceTest {
    @InjectMocks
    CategoryServiceImpl categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    Environment environment;

    @Spy
    MapStructMapper mapStructMapper = Mappers.getMapper(MapStructMapper.class);

    @Spy
    MapStructMapperUpdate mapStructMapperUpdate = Mappers.getMapper(MapStructMapperUpdate.class);

    @Test
    void shouldReturnListOfAllCategories() {
        Category categoryA = new Category(1L, "Category A", true);
        Category categoryA1 = new Category(2L, "Category A1", true);
        Category categoryA2 = new Category(  3L, "Category A2", true);

        categoryA1.setParentCategory(categoryA);
        categoryA2.setParentCategory(categoryA);

        List<Category> allCategories = List.of(categoryA, categoryA1, categoryA2);

        when(categoryRepository.findAll()).thenReturn(allCategories);

        List<Category> returnedCategories = categoryService.getAllCategories();

        assertThat(returnedCategories.size()).isEqualTo(3);
    }

    @Test
    void shouldReturnAllRootCategories() {
        Category categoryA = new Category(1L, "Category A", true);
        Category categoryA1 = new Category(2L, "Category A1", true);
        Category categoryA2 = new Category(  3L, "Category A2", true);

        categoryA1.setParentCategory(categoryA);
        categoryA2.setParentCategory(categoryA);

        List<Category> rootCategories = List.of(categoryA);

        when(categoryRepository.findCategoriesByParentCategoryIsNull()).thenReturn(rootCategories);

        List<Category> returnedCategories = categoryService.getAllRootCategories();

        assertThat(returnedCategories.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnAllLeafCategories() {
        Category categoryA = new Category(1L, "Category A", true);
        Category categoryA1 = new Category(2L, "Category A1", true);
        Category categoryA2 = new Category(  3L, "Category A2", true);

        categoryA1.setParentCategory(categoryA);
        categoryA2.setParentCategory(categoryA);

        List<Category> leafCategories = List.of(categoryA1, categoryA2);

        when(categoryRepository.findCategoriesBySubCategoriesIsNull()).thenReturn(leafCategories);

        List<Category> returnedCategories = categoryService.getAllLeafCategories();

        assertThat(returnedCategories.size()).isEqualTo(2);
    }

    @Test
    void shouldReturnAllUpperCategories_whenValidIdIsGiven() {
        Category categoryA = new Category(1L, "Category A", true);
        Category categoryA1 = new Category(2L, "Category A1", true);
        Category categoryA2 = new Category(  3L, "Category A2", true);

        categoryA1.setParentCategory(categoryA);
        categoryA2.setParentCategory(categoryA1);

        List<Category> leafCategories = List.of(categoryA, categoryA1);

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryA2));

        List<Category> returnedCategories = categoryService.getAllUpperCategoriesById(3L);

        assertThat(returnedCategories.size()).isEqualTo(3);
    }

    @Test
    void shouldReturnPageOfAllRootCategories() {
        Category categoryA = new Category(1L, "Category A", true);
        Category categoryA1 = new Category(2L, "Category A1", true);
        Category categoryA2 = new Category(  3L, "Category A2", true);

        categoryA1.setParentCategory(categoryA);
        categoryA2.setParentCategory(categoryA);

        Page<Category> actualPage = new PageImpl<>(List.of(categoryA));

        when(categoryRepository.findCategoriesByParentCategoryIsNull(any(Pageable.class))).thenReturn(actualPage);

        Page<Category> foundPage = categoryService.getAllRootCategoriesPageable(1, 10);

        assertThat(foundPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldReturnPageOfAllSubCategories_whenIdIsGiven() {
        Category categoryA = new Category(1L, "Category A", true);
        Category categoryA1 = new Category(2L, "Category A1", true);
        Category categoryA2 = new Category(  3L, "Category A2", true);

        categoryA1.setParentCategory(categoryA);
        categoryA2.setParentCategory(categoryA);

        Page<Category> actualPage = new PageImpl<>(List.of(categoryA1, categoryA2));

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryA));
        when(categoryRepository.findSubCategories(anyLong(), any(Pageable.class))).thenReturn(actualPage);

        Page<Category> foundPage = categoryService.getAllSubCategoriesPageable(1L, 1, 10);

        assertThat(foundPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    void shouldReturnCategory_whenIdIsGiven() {
        Category categoryA = new Category(1L, "Category A", true);

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryA));

        Category returnedCategory = categoryService.getCategoryById(1L);

        assertThat(returnedCategory).isEqualTo(categoryA);
    }

    @Test
    void shouldReturnCategory_whenNameIsGiven() {
        Category categoryA = new Category(1L, "Category A", true);

        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(categoryA));

        Category returnedCategory = categoryService.getCategoryByName("Category A");

        assertThat(returnedCategory).isEqualTo(categoryA);
    }

    @Test
    void shouldSaveNewCategory_whenValidInputsAreGiven() {
        CategoryDto categoryDto = new CategoryDto("Category A", true);

        when(categoryRepository.findByName(anyString())).thenThrow(CategoryNotFoundException.class);

        categoryService.createNewCategory(categoryDto, null);

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void shouldUpdateCategory_whenValidInputsAreGiven() {
        Category category = new Category(1L, "Category A", true);
        CategoryDto categoryDto = new CategoryDto("Category B");

        when(categoryRepository.findCategoriesByName(anyString())).thenReturn(List.of(category));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        categoryService.updateCategory(1L, categoryDto, null);

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void shouldDeleteCategory_whenValidIdIsGiven() {
        Category category = new Category(1L, "Category A", true);

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        categoryService.deleteCategoryById(1L);

        verify(categoryRepository, times(1)).deleteById(anyLong());
    }
}