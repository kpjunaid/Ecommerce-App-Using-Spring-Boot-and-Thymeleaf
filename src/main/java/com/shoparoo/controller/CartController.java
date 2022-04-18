package com.shoparoo.controller;

import com.shoparoo.entity.CartItem;
import com.shoparoo.entity.Product;
import com.shoparoo.entity.User;
import com.shoparoo.exception.CartItemExistsException;
import com.shoparoo.exception.UserNotFoundException;
import com.shoparoo.service.*;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductService productService;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public CartController(CartService cartService,
                          UserService userService,
                          CategoryService categoryService,
                          BrandService brandService,
                          ProductService productService,
                          MessageSource messageSource,
                          LocaleResolver localeResolver) {
        this.cartService = cartService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.productService = productService;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @GetMapping("/cart")
    public String showViewCartPage(HttpServletRequest request,
                                   Authentication authentication,
                                   Model model) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            List<CartItem> cartItems = cartService.getAllCartItemsByUser(user);
            model.addAttribute("cartItems", cartItems);

            model.addAttribute("pageTitle", messageSource.getMessage(
                    "pageTitle.cart",
                    null,
                    localeResolver.resolveLocale(request)));
            model.addAttribute("navCategories", categoryService.getAllLeafCategories());
            model.addAttribute("navBrands", brandService.getAllBrands());
            userService.addAuthUserToModel(authentication, model);
            cartService.addCartItemsToModel(authentication, model);

            return "cart";
        } catch (UserNotFoundException e) {
            return "redirect:/error";
        }
    }

    @PostMapping("/cart/item/add")
    public ResponseEntity<Object> addNewCartItem(@RequestParam("userId") Long userId,
                                                 @RequestParam("productId") Long productId,
                                                 @RequestParam("quantity") Integer quantity,
                                                 HttpServletRequest request) {
        try {
            User user = userService.getUserById(userId);
            Product product = productService.getProductById(productId);
            cartService.createNewCartItem(user, product, quantity);

            return new ResponseEntity<>(messageSource.getMessage(
                    "response.cartItemAddSuccess",
                    null,
                    localeResolver.resolveLocale(request)), HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e instanceof CartItemExistsException) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.exception.cartItemExistsException",
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

    @PostMapping("/cart/item/delete")
    public ResponseEntity<Object> deleteCartItem(@RequestParam("productId") Long productId,
                                                 HttpServletRequest request,
                                                 Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            Product product = productService.getProductById(productId);
            cartService.deleteCartItemByUserAndProduct(user, product);

            return new ResponseEntity<>(messageSource.getMessage(
                    "response.cartItemDeleteSuccess",
                    null,
                    localeResolver.resolveLocale(request)), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(messageSource.getMessage(
                    "response.serverError",
                    null,
                    localeResolver.resolveLocale(request)), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cart/update")
    public ResponseEntity<Object> updateCart(@RequestParam("productId") Long productId,
                                             @RequestParam("quantity") Integer quantity,
                                             HttpServletRequest request,
                                             Authentication authentication) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            Product product = productService.getProductById(productId);
            cartService.changeCartItemQuantity(user, product, quantity);

            return new ResponseEntity<>(messageSource.getMessage(
                    "response.cartItemUpdateSuccess",
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
