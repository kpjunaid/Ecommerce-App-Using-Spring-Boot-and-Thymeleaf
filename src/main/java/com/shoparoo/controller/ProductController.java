package com.shoparoo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shoparoo.dto.ProductDto;
import com.shoparoo.entity.Category;
import com.shoparoo.entity.Product;
import com.shoparoo.exception.ProductNotFoundException;
import com.shoparoo.exception.ProductPhotoLimitException;
import com.shoparoo.exception.ProductPhotoNotFoundException;
import com.shoparoo.mapper.MapStructMapper;
import com.shoparoo.service.*;
import com.shoparoo.util.BindingResultErrorUtil;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class ProductController {
    private final Integer CONTENT_PER_PAGE = 10;
    private final ProductService productService;
    private final ProductPhotoService productPhotoService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CartService cartService;
    private final MapStructMapper mapStructMapper;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public ProductController(ProductService productService,
                             ProductPhotoService productPhotoService,
                             BrandService brandService,
                             CategoryService categoryService,
                             UserService userService,
                             CartService cartService,
                             MapStructMapper mapStructMapper,
                             MessageSource messageSource,
                             LocaleResolver localeResolver) {
        this.productService = productService;
        this.productPhotoService = productPhotoService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.cartService = cartService;
        this.mapStructMapper = mapStructMapper;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @GetMapping("/products")
    public String showProductsPage() {
        return "redirect:/products/page/1";
    }

    @GetMapping("/products/page/{pageNumber}")
    public String showProductsPageCriteriaByPage(@PathVariable("pageNumber") Integer pageNumber,
                                                 @RequestParam(value = "search", required = false) String search,
                                                 @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                 @RequestParam(value = "brandId", required = false) Long brandId,
                                                 @RequestParam(value = "sort", required = false) String sort,
                                                 @RequestParam(value = "dir", required = false) String dir,
                                                 HttpServletRequest request,
                                                 Authentication authentication,
                                                 Model model) {
        if (pageNumber == null || pageNumber < 1)  pageNumber = 1;
        if (sort == null) sort = "name";
        if (dir == null) dir = "asc";


        model.addAttribute("category", null);
        model.addAttribute("categories", null);
        model.addAttribute("brand", null);
        model.addAttribute("brands", null);

        model.addAttribute("sort", sort.equals("resalePrice") ? "Price" : StringUtils.capitalize(sort));
        model.addAttribute("dir", StringUtils.capitalize(dir));

        StringBuilder requestParamString = new StringBuilder();
        StringBuilder filterParamString = new StringBuilder();
        requestParamString.append("?");
        filterParamString.append("?");
        Page<Product> foundPage;

        if (categoryId != null) {
            requestParamString.append("categoryId=").append(categoryId);
            filterParamString.append("categoryId=").append(categoryId);
            foundPage = productService.getAllProductsByCategoryPageable(pageNumber, categoryId, sort, dir, CONTENT_PER_PAGE);
            if (foundPage.hasContent()) {
                Category category = categoryService.getCategoryById(categoryId);
                model.addAttribute("category", category);
                model.addAttribute("brands", category.getBrands());
            }
        } else if (brandId != null) {
            requestParamString.append("brandId=").append(brandId);
            filterParamString.append("brandId=").append(brandId);
            foundPage = productService.getAllProductsByBrandPageable(pageNumber, brandId, sort, dir, CONTENT_PER_PAGE);
            if (foundPage.hasContent()) {
                model.addAttribute("brand", brandService.getBrandById(brandId));
            }
        } else {
            if (search != null) {
                foundPage = productService.searchProductsPageable(pageNumber, search, sort, dir, CONTENT_PER_PAGE);
                requestParamString.append("&search=").append(search);
            } else {
                foundPage = productService.getAllProductsPageable(pageNumber, sort, dir, CONTENT_PER_PAGE);
            }
            model.addAttribute("categories", categoryService.getAllLeafCategories());
        }

        List<ProductDto> products = foundPage.stream()
                .map(mapStructMapper::productToProductDto).collect(Collectors.toList());

        requestParamString.append("&sort=").append(sort).append("&dir=").append(dir);

        model.addAttribute("filterParamString", filterParamString);
        model.addAttribute("requestParamString", requestParamString);
        model.addAttribute("products", products);

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
                "pageTitle.product.list",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "product-list";
    }

    @GetMapping("/products/{productId}")
    public String showProductSinglePage(@PathVariable("productId") Long id,
                                        HttpServletRequest request,
                                        Authentication authentication,
                                        Model model) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", product.getName() + messageSource.getMessage(
                    "pageTitle.siteName",
                    null,
                    localeResolver.resolveLocale(request)));
        } catch (ProductNotFoundException e) {
            return "redirect:/error";
        }


        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "product-single";
    }

    @PostMapping("/products/search")
    public ResponseEntity<List<ObjectNode>> searchProducts(@RequestParam("search") String search) {
        int pageNumber = 1;
        String sort = "name";
        String dir = "asc";
        List<ObjectNode> resultList = new ArrayList<>();
        ObjectMapper searchResult = new ObjectMapper();
        Page<Product> foundPage = productService.searchProductsPageable(pageNumber, search, sort, dir, CONTENT_PER_PAGE);
        for (Product product: foundPage.getContent()) {
            ObjectNode result = searchResult.createObjectNode();
            result.put("id", product.getId());
            result.put("name", product.getName());
            result.put("price", product.getResalePrice());
            if (product.getProductPhotos().size() > 0) {
                result.put("photoUrl", product.getProductPhotos().get(0).getPhotoUrl());
            } else {
                result.put("photoUrl", false);
            }
            resultList.add(result);
        }
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping("/admin/products")
    public String adminListProducts() {
        return "redirect:/admin/products/page/1";
    }

    @GetMapping("/admin/products/page/{pageNumber}")
    public String adminListProductsPageable(@PathVariable("pageNumber") Integer pageNumber,
                                     @RequestParam(value = "sort", required = false) String sort,
                                     @RequestParam(value = "dir", required = false) String dir,
                                     HttpServletRequest request,
                                     Authentication authentication,
                                     Model model) {
        if (pageNumber == null || pageNumber < 1)  pageNumber = 1;
        if (sort == null) sort = "name";
        if (dir == null) dir = "asc";

        Page<Product> foundPage = productService.getAllProductsPageable(pageNumber, sort, dir, CONTENT_PER_PAGE);
        List<ProductDto> products = foundPage.stream()
                .map(mapStructMapper::productToProductDto).collect(Collectors.toList());

        model.addAttribute("products", products);
        userService.addAuthUserToModel(authentication, model);

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
                "pageTitle.admin.product.list",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "admin-product-list";
    }

    @GetMapping("/admin/products/create")
    public String adminShowCreateProductPage(HttpServletRequest request,
                                             Authentication authentication,
                                             Model model) {
        ProductDto productDto = new ProductDto();
        productDto.setEnabled(true);
        model.addAttribute("product", productDto);
        model.addAttribute("categories", categoryService.getAllHierarchicalCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("productUpdate", false);

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.admin.product.create",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "admin-product-form";
    }

    @PostMapping("/admin/products/create")
    public ResponseEntity<Object> adminSaveNewProduct(@Valid ProductDto productDto,
                                                      BindingResult bindingResult,
                                                      @RequestParam(value = "productPhoto1") Optional<MultipartFile> productPhoto1,
                                                      @RequestParam(value = "productPhoto2") Optional<MultipartFile> productPhoto2,
                                                      @RequestParam(value = "productPhoto3") Optional<MultipartFile> productPhoto3,
                                                      HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return BindingResultErrorUtil.getBindingResultErrorResponse(bindingResult);
        } else {
            try {
                productService.createNewProduct(productDto, List.of(productPhoto1, productPhoto2, productPhoto3));
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.productCreateSuccess",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.OK);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.serverError",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/admin/products/update/{productId}")
    public String adminShowUpdateProductPage(@PathVariable("productId") Long id,
                                             HttpServletRequest request,
                                             Authentication authentication,
                                             Model model) {
        try {
            ProductDto productDto = mapStructMapper.productToProductDto(productService.getProductById(id));

            model.addAttribute("product", productDto);
            model.addAttribute("categories", categoryService.getAllHierarchicalCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("productUpdate", true);

            model.addAttribute("pageTitle", messageSource.getMessage(
                    "pageTitle.admin.product.update",
                    null,
                    localeResolver.resolveLocale(request)));
            model.addAttribute("navCategories", categoryService.getAllLeafCategories());
            model.addAttribute("navBrands", brandService.getAllBrands());
            userService.addAuthUserToModel(authentication, model);
            cartService.addCartItemsToModel(authentication, model);

            return "admin-product-form";
        } catch (ProductNotFoundException e) {
            return "redirect:/error";
        }
    }

    @PostMapping("/admin/products/update")
    public ResponseEntity<Object> adminUpdateProduct(@Valid ProductDto productDto,
                                                     BindingResult bindingResult,
                                                     @RequestParam("productId") Long id,
                                                     @RequestParam(value = "productPhoto1") Optional<MultipartFile> productPhoto1,
                                                     @RequestParam(value = "productPhoto2") Optional<MultipartFile> productPhoto2,
                                                     @RequestParam(value = "productPhoto3") Optional<MultipartFile> productPhoto3,
                                                     HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return BindingResultErrorUtil.getBindingResultErrorResponse(bindingResult);
        } else {
            try {
                productService.updateProduct(id, productDto, List.of(productPhoto1, productPhoto2, productPhoto3));
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.productUpdateSuccess",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.OK);
            } catch (RuntimeException e) {
                if (e instanceof ProductNotFoundException) {
                    return new ResponseEntity<>(messageSource.getMessage(
                            "response.exception.productNotFoundException",
                            null,
                            localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
                } else if (e instanceof ProductPhotoLimitException) {
                    return new ResponseEntity<>(messageSource.getMessage(
                            "response.exception.productPhotoLimitException",
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

    @PostMapping("/admin/products/delete")
    public ResponseEntity<Object> adminDeleteProduct(@RequestParam("productId") Long id,
                                                     Authentication authentication,
                                                     HttpServletRequest request) {
        try {
            productService.deleteProductById(id);
            return new ResponseEntity<>(messageSource.getMessage(
                    "response.productDeleteSuccess",
                    null,
                    localeResolver.resolveLocale(request)), HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e instanceof ProductNotFoundException) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.exception.productNotFoundException",
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

    @PostMapping("/admin/products/photos/delete")
    public ResponseEntity<Object> adminDeleteProductPhoto(@RequestParam("productPhotoId") Long id,
                                                          Authentication authentication,
                                                          HttpServletRequest request) {
        try {
            productPhotoService.deleteProductPhotoById(id);
            return new ResponseEntity<>(messageSource.getMessage(
                    "response.productPhotoDeleteSuccess",
                    null,
                    localeResolver.resolveLocale(request)), HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e instanceof ProductPhotoNotFoundException) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.exception.productPhotoDeleteException",
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
