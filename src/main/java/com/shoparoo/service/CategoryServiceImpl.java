package com.shoparoo.service;

import com.shoparoo.dto.CategoryDto;
import com.shoparoo.entity.Category;
import com.shoparoo.exception.CategoryExistsException;
import com.shoparoo.exception.CategoryNotFoundException;
import com.shoparoo.mapper.MapStructMapper;
import com.shoparoo.mapper.MapStructMapperUpdate;
import com.shoparoo.repository.CategoryRepository;
import com.shoparoo.util.FileNamingUtil;
import com.shoparoo.util.FileUploadUtil;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final MapStructMapper mapStructMapper;
    private final MapStructMapperUpdate mapStructMapperUpdate;
    private final Environment environment;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               MapStructMapper mapStructMapper,
                               MapStructMapperUpdate mapStructMapperUpdate,
                               Environment environment) {
        this.categoryRepository = categoryRepository;
        this.mapStructMapper = mapStructMapper;
        this.mapStructMapperUpdate = mapStructMapperUpdate;
        this.environment = environment;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getAllRootCategories() {
        return categoryRepository.findCategoriesByParentCategoryIsNull();
    }

    @Override
    public List<Category> getAllLeafCategories() {
        return categoryRepository.findCategoriesBySubCategoriesIsNull();
    }

    @Override
    public List<CategoryDto> getAllHierarchicalCategories() {
        List<CategoryDto> categoryList = new ArrayList<>();
        List<Category> categoryListInDb = categoryRepository.findAll(Sort.by("name").ascending());

        formatCategoryList(categoryList, categoryListInDb);

        return categoryList;
    }

    private void formatCategoryList(List<CategoryDto> categoryList,
                                    List<Category> categoryListInDb) {
        for (Category category: categoryListInDb) {
            if (category.getParentCategory() == null) {
                categoryList.add(new CategoryDto(category.getId(),
                        category.getName(),
                        category.getPhotoUrl(),
                        category.getEnabled()));

                List<Category> subCategories = category.getSubCategories()
                        .stream()
                        .sorted(Comparator.comparing(Category::getName))
                        .collect(Collectors.toList());
                for (Category subCategory: subCategories) {
                    categoryList.add(new CategoryDto(subCategory.getId(),
                            "--" + subCategory.getName(),
                            subCategory.getPhotoUrl(),
                            subCategory.getEnabled()));

                    getSubCategories(subCategory, 1, categoryList);
                }
            }
        }
    }

    private void getSubCategories(Category parentCategory,
                                   Integer level,
                                   List<CategoryDto> categoryList) {
        level++;
        List<Category> subCategories = parentCategory.getSubCategories()
                .stream()
                .sorted(Comparator.comparing(Category::getName))
                .collect(Collectors.toList());

        for (Category subCategory: subCategories) {
            StringBuilder categoryName = new StringBuilder();
            for (int i = 0; i < level; i++) {
                categoryName.append("--");
            }
            categoryList.add(new CategoryDto(subCategory.getId(),
                    categoryName + subCategory.getName(),
                    subCategory.getPhotoUrl(),
                    subCategory.getEnabled()));

            getSubCategories(subCategory, level, categoryList);
        }
    }

    @Override
    public List<Category> getAllUpperCategoriesById(Long id) {
        try {
            Category category = getCategoryById(id);
            List<Category> parentCategoryList = new ArrayList<>();
            parentCategoryList.add(category);
            while (category.getParentCategory() != null) {
                parentCategoryList.add(category.getParentCategory());
                category = category.getParentCategory();
            }
            return parentCategoryList;
        } catch (CategoryNotFoundException e) {
            throw new CategoryNotFoundException();
        }
    }

    @Override
    public Page<Category> getAllRootCategoriesPageable(Integer pageNumber, Integer contentPerPage) {
        if (pageNumber <= 0) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, contentPerPage, Sort.by("name").ascending());
        Page<Category> foundPage = categoryRepository.findCategoriesByParentCategoryIsNull(pageable);

        if (foundPage.hasContent()) {
            return foundPage;
        } else {
            return Page.empty();
        }
    }

    @Override
    public Page<Category> getAllSubCategoriesPageable(Long parentId, Integer pageNumber, Integer contentPerPage) {
        try {
            if (pageNumber <= 0) {
                return Page.empty();
            }
            Pageable pageable = PageRequest.of(pageNumber - 1, contentPerPage, Sort.by("name").ascending());

            Category category = getCategoryById(parentId);

            return categoryRepository.findSubCategories(parentId, pageable);
        } catch (CategoryNotFoundException e) {
            throw new CategoryNotFoundException();
        }
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public void createNewCategory(CategoryDto categoryDto, MultipartFile categoryPhoto) {
        try {
            Category category = getCategoryByName(categoryDto.getName());
            if (category != null) {
                throw new CategoryExistsException();
            }
        } catch (CategoryNotFoundException e) {
            if (categoryPhoto != null) {
                String dpUploadDir = environment.getProperty("photo.upload.category");
                categoryDto.setPhotoUrl(FileNamingUtil.nameFile(categoryPhoto));
                try {
                    FileUploadUtil.saveNewFile(dpUploadDir, categoryDto.getPhotoUrl(), categoryPhoto);
                } catch (IOException ignored) {
                    throw new RuntimeException();
                }
            }

            Category category = mapStructMapper.categoryDtoToCategory(categoryDto);
            categoryRepository.save(category);
        }
    }

    @Override
    public void updateCategory(Long id, CategoryDto categoryDto, MultipartFile categoryPhoto) {
        try {
            List<Category> duplicateCategories = categoryRepository.findCategoriesByName(categoryDto.getName());
            if (duplicateCategories.size() > 1) {
                throw new CategoryExistsException();
            }

            Category category = getCategoryById(id);

            if (categoryPhoto != null) {
                String dpUploadDir = environment.getProperty("photo.upload.category");
                categoryDto.setPhotoUrl(FileNamingUtil.nameFile(categoryPhoto));
                try {
                    if (category.getPhotoUrl() == null) {
                        FileUploadUtil.saveNewFile(dpUploadDir, categoryDto.getPhotoUrl(), categoryPhoto);
                    } else {
                        FileUploadUtil.updateFile(dpUploadDir, categoryDto.getPhotoUrl(),
                                categoryDto.getPhotoUrl(), categoryPhoto);
                    }
                } catch (IOException ignored) {
                    throw new RuntimeException();
                }
            } else {
                categoryDto.setPhotoUrl(category.getPhotoUrl());
            }

            mapStructMapperUpdate.adminUpdateCategoryFromCategoryDto(categoryDto, category);
            categoryRepository.save(category);
        } catch (CategoryNotFoundException e) {
            throw new CategoryExistsException();
        }
    }

    @Override
    public void deleteCategoryById(Long id) {
        try {
            Category category = getCategoryById(id);
            if (category != null) {
                String photo = category.getPhotoUrl();
                // delete category photo from filesystem if exists
                if (photo != null && photo.length() > 0) {
                    String photoUploadDir = environment.getProperty("photo.upload.category");
                    try {
                        FileUploadUtil.deleteFile(photoUploadDir, photo);
                    } catch (IOException e) {
                        throw new RuntimeException();
                    }
                }

                categoryRepository.deleteById(id);
            }
        } catch (CategoryNotFoundException e) {
            throw new CategoryNotFoundException();
        }
    }
}
