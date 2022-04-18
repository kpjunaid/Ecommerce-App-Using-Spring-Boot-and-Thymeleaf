package com.shoparoo.controller;

import com.shoparoo.dto.BrandDto;
import com.shoparoo.entity.Brand;
import com.shoparoo.exception.BrandExistsException;
import com.shoparoo.exception.BrandNotFoundException;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class BrandController {
    private final Integer CONTENT_PER_PAGE = 10;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final CartService cartService;
    private final UserService userService;
    private final MapStructMapper mapStructMapper;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public BrandController(BrandService brandService,
                           CategoryService categoryService,
                           CartService cartService,
                           UserService userService,
                           MapStructMapper mapStructMapper,
                           MessageSource messageSource,
                           LocaleResolver localeResolver) {
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.cartService = cartService;
        this.userService = userService;
        this.mapStructMapper = mapStructMapper;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @GetMapping("/admin/brands")
    public String adminListBrands() {
        return "redirect:/admin/brands/page/1";
    }

    @GetMapping("/admin/brands/page/{pageNumber}")
    public String adminListBrandsPageable(@PathVariable("pageNumber") Integer pageNumber,
                                          HttpServletRequest request,
                                          Authentication authentication,
                                          Model model) {
        if (pageNumber == null || pageNumber < 1)  pageNumber = 1;

        Page<Brand> foundPage = brandService.getAllBrandsPageable(pageNumber, CONTENT_PER_PAGE);
        List<BrandDto> brands = foundPage.stream()
                .map(mapStructMapper::brandToBrandDto).collect(Collectors.toList());

        model.addAttribute("brands", brands);

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
                "pageTitle.admin.brand.list",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "admin-brand-list";
    }

    @GetMapping("/admin/brands/create")
    public String adminShowCreateBrandPage(HttpServletRequest request,
                                           Authentication authentication,
                                           Model model) {
        BrandDto brandDto = new BrandDto();
        brandDto.setEnabled(true);
        model.addAttribute("brand", brandDto);
        model.addAttribute("allCategories", categoryService.getAllHierarchicalCategories());
        model.addAttribute("brandUpdate", false);

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.admin.brand.create",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "admin-brand-form";
    }

    @PostMapping("/admin/brands/create")
    public ResponseEntity<Object> adminSaveNewBrand(@Valid BrandDto brandDto,
                                                  BindingResult bindingResult,
                                                  @RequestParam(value = "brandPhoto", required = false) MultipartFile brandPhoto,
                                                  HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return BindingResultErrorUtil.getBindingResultErrorResponse(bindingResult);
        } else {
            try {
                brandService.createNewBrand(brandDto, brandPhoto);
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.brandCreateSuccess",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.OK);
            } catch (RuntimeException e) {
                if (e instanceof BrandExistsException) {
                    return new ResponseEntity<>(messageSource.getMessage(
                            "response.exception.brandExistsException",
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

    @GetMapping("/admin/brands/update/{brandId}")
    public String adminShowUpdateBrandPage(@PathVariable("brandId") Long id,
                                           HttpServletRequest request,
                                           Authentication authentication,
                                           Model model) {
        try {
            BrandDto brandDto = mapStructMapper.brandToBrandDto(brandService.getBrandById(id));

            model.addAttribute("brand", brandDto);
            model.addAttribute("allCategories", categoryService.getAllHierarchicalCategories());
            model.addAttribute("brandUpdate", true);

            model.addAttribute("pageTitle", messageSource.getMessage(
                    "pageTitle.admin.brand.update",
                    null,
                    localeResolver.resolveLocale(request)));
            model.addAttribute("navCategories", categoryService.getAllLeafCategories());
            model.addAttribute("navBrands", brandService.getAllBrands());
            userService.addAuthUserToModel(authentication, model);
            cartService.addCartItemsToModel(authentication, model);

            return "admin-brand-form";
        } catch (BrandNotFoundException e) {
            return "redirect:/error";
        }
    }

    @PostMapping("/admin/brands/update")
    public ResponseEntity<Object> updateBrand(@Valid BrandDto brandDto,
                                              BindingResult bindingResult,
                                              @RequestParam("brandId") Long id,
                                              @RequestParam(value = "brandPhoto", required = false) MultipartFile brandPhoto,
                                              HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return BindingResultErrorUtil.getBindingResultErrorResponse(bindingResult);
        } else {
            try {
                brandService.updateBrand(id, brandDto, brandPhoto);

                return new ResponseEntity<>(messageSource.getMessage(
                        "response.brandCreateSuccess",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.OK);
            } catch (RuntimeException e) {
                if (e instanceof BrandNotFoundException) {
                    return new ResponseEntity<>(messageSource.getMessage(
                            "response.exception.brandNotFoundException",
                            null,
                            localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
                } else if (e instanceof BrandExistsException) {
                    return new ResponseEntity<>(messageSource.getMessage(
                            "response.exception.brandExistsException",
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

    @PostMapping("/admin/brands/delete")
    public ResponseEntity<String> deleteBrand(@RequestParam("brandId") Long id,
                                              HttpServletRequest request) {
        try {
            brandService.deleteBrandById(id);
            return new ResponseEntity<>(messageSource.getMessage(
                    "response.brandDeleteSuccess",
                    null,
                    localeResolver.resolveLocale(request)), HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e instanceof BrandNotFoundException) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.exception.brandNotFoundException",
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
