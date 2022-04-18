package com.shoparoo.controller;

import com.shoparoo.dto.CategoryDto;
import com.shoparoo.entity.Category;
import com.shoparoo.exception.CategoryExistsException;
import com.shoparoo.exception.CategoryNotFoundException;
import com.shoparoo.mapper.MapStructMapper;
import com.shoparoo.service.BrandService;
import com.shoparoo.service.CartService;
import com.shoparoo.service.CategoryService;
import com.shoparoo.service.UserService;
import com.shoparoo.util.BindingResultErrorUtil;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class CategoryController {
    private final Integer CONTENT_PER_PAGE = 10;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final CartService cartService;
    private final UserService userService;
    private final MapStructMapper mapStructMapper;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public CategoryController(CategoryService categoryService,
                              BrandService brandService,
                              CartService cartService,
                              UserService userService,
                              MapStructMapper mapStructMapper,
                              MessageSource messageSource,
                              LocaleResolver localeResolver) {
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.cartService = cartService;
        this.userService = userService;
        this.mapStructMapper = mapStructMapper;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @GetMapping("/admin/categories")
    public String adminListCategories() {
        return "redirect:/admin/categories/page/1";
    }

    @GetMapping("/admin/categories/page/{pageNumber}")
    public String adminListCategoriesPageable(@PathVariable("pageNumber") Integer pageNumber,
                                              HttpServletRequest request,
                                              Authentication authentication,
                                              Model model) {
        if (pageNumber == null || pageNumber < 1)  pageNumber = 1;

        Page<Category> foundPage = categoryService.getAllRootCategoriesPageable(pageNumber, CONTENT_PER_PAGE);
        List<CategoryDto> categoryList = foundPage.stream()
                .map(mapStructMapper::categoryToCategoryDto).collect(Collectors.toList());

        model.addAttribute("categories", categoryList);

        int currentPage = pageNumber;
        int contentCount = (int) foundPage.getTotalElements();
        int pageCount = foundPage.getTotalPages();
        long startCount = (long) (currentPage - 1) * CONTENT_PER_PAGE + 1;
        long endCount = startCount + CONTENT_PER_PAGE - 1;

        if (endCount > contentCount) {
            endCount = contentCount;
        }

        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("contentPerPage", CONTENT_PER_PAGE);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("contentCount", contentCount);
        model.addAttribute("pageCount", pageCount);

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.admin.category.list",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "admin-category-list";
    }

    @GetMapping("/admin/categories/{categoryId}/children")
    public String adminListSubCategories(@PathVariable("categoryId") Long id,
                                          Authentication authentication,
                                          Model model) {
        return "redirect:/admin/categories/" + id + "/children/page/1";
    }

    @GetMapping("/admin/categories/{categoryId}/children/page/{pageNumber}")
    public String adminListSubCategoriesPageable(@PathVariable("categoryId") Long id,
                                                 @PathVariable("pageNumber") Integer pageNumber,
                                                 HttpServletRequest request,
                                                 Authentication authentication,
                                                 Model model) {
        if (pageNumber == null || pageNumber < 1)  pageNumber = 1;

        Page<Category> foundPage = categoryService.getAllSubCategoriesPageable(id, pageNumber, CONTENT_PER_PAGE);
        List<CategoryDto> categoryList = foundPage.stream()
                .map(mapStructMapper::categoryToCategoryDto).collect(Collectors.toList());

        List<Category> parentCategoryList = categoryService.getAllUpperCategoriesById(id);
        Collections.reverse(parentCategoryList);
        model.addAttribute("parentCategoryList", parentCategoryList);

        model.addAttribute("categories", categoryList);

        int currentPage = pageNumber;
        int contentCount = (int) foundPage.getTotalElements();
        int pageCount = foundPage.getTotalPages();
        long startCount = (long) (currentPage - 1) * CONTENT_PER_PAGE + 1;
        long endCount = startCount + CONTENT_PER_PAGE - 1;

        if (endCount > contentCount) {
            endCount = contentCount;
        }

        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("contentPerPage", CONTENT_PER_PAGE);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("contentCount", contentCount);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("subCategoryId", id);

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.admin.category.list",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "admin-category-list";
    }

    @GetMapping("/admin/categories/create")
    public String adminShowCreateCategoryPage(HttpServletRequest request,
                                              Authentication authentication,
                                              Model model) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setEnabled(true);
        model.addAttribute("category", categoryDto);
        model.addAttribute("allCategories", categoryService.getAllHierarchicalCategories());
        model.addAttribute("categoryUpdate", false);

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.admin.category.create",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "admin-category-form";
    }

    @PostMapping("/admin/categories/create")
    public ResponseEntity<Object> adminSaveNewCategory(@Valid CategoryDto categoryDto,
                                                  BindingResult bindingResult,
                                                  @RequestParam(value = "categoryPhoto", required = false) MultipartFile categoryPhoto,
                                                  HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return BindingResultErrorUtil.getBindingResultErrorResponse(bindingResult);
        } else {
            try {
                categoryService.createNewCategory(categoryDto, categoryPhoto);
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.categoryCreateSuccess",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.OK);
            } catch (RuntimeException e) {
                if (e instanceof CategoryExistsException) {
                    return new ResponseEntity<>(messageSource.getMessage(
                            "response.exception.categoryExistsException",
                            null,
                            localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
                } else {
                    return new ResponseEntity<>(messageSource.getMessage(
                            "response.serverError",
                            null,
                            localeResolver.resolveLocale(request)), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
    }

    @GetMapping("/admin/categories/update/{categoryId}")
    public String adminShowUpdateCategoryPage(@PathVariable("categoryId") Long id,
                                              HttpServletRequest request,
                                              Authentication authentication,
                                              Model model) {
        try {
            CategoryDto categoryDto = mapStructMapper.categoryToCategoryDto(categoryService.getCategoryById(id));

            model.addAttribute("category", categoryDto);
            model.addAttribute("allCategories", categoryService.getAllHierarchicalCategories());
            model.addAttribute("categoryUpdate", true);

            model.addAttribute("pageTitle", messageSource.getMessage(
                    "pageTitle.admin.category.update",
                    null,
                    localeResolver.resolveLocale(request)));
            model.addAttribute("navCategories", categoryService.getAllLeafCategories());
            model.addAttribute("navBrands", brandService.getAllBrands());
            userService.addAuthUserToModel(authentication, model);
            cartService.addCartItemsToModel(authentication, model);

            return "admin-category-form";
        } catch (CategoryNotFoundException e) {
            return "redirect:/error";
        }
    }

    @PostMapping("/admin/categories/update")
    public ResponseEntity<Object> adminUpdateCategory(@Valid CategoryDto categoryDto,
                                                      BindingResult bindingResult,
                                                      @RequestParam("categoryId") Long id,
                                                      @RequestParam(value = "categoryPhoto", required = false) MultipartFile categoryPhoto,
                                                      HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return BindingResultErrorUtil.getBindingResultErrorResponse(bindingResult);
        } else {
            try {
                categoryService.updateCategory(id, categoryDto, categoryPhoto);

                return new ResponseEntity<>(messageSource.getMessage(
                        "response.categoryCreateSuccess",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.OK);
            } catch (RuntimeException e) {
                if (e instanceof CategoryNotFoundException) {
                    return new ResponseEntity<>(messageSource.getMessage(
                            "response.exception.categoryNotFoundException",
                            null,
                            localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
                } else if (e instanceof CategoryExistsException) {
                    return new ResponseEntity<>(messageSource.getMessage(
                            "response.exception.categoryExistsException",
                            null,
                            localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
                } else {
                    return new ResponseEntity<>(messageSource.getMessage(
                            "response.serverError",
                            null,
                            localeResolver.resolveLocale(request)), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
    }

    @PostMapping("/admin/category/delete")
    public ResponseEntity<String> deleteUser(@RequestParam("categoryId") Long id,
                                             HttpServletRequest request) {
        try {
            categoryService.deleteCategoryById(id);
            return new ResponseEntity<>(messageSource.getMessage(
                    "response.categoryDeleteSuccess",
                    null,
                    localeResolver.resolveLocale(request)), HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e instanceof CategoryNotFoundException) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.exception.categoryNotFoundException",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.serverError",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
