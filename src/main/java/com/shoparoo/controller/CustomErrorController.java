package com.shoparoo.controller;

import com.shoparoo.service.BrandService;
import com.shoparoo.service.CartService;
import com.shoparoo.service.CategoryService;
import com.shoparoo.service.UserService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping
public class CustomErrorController implements ErrorController {
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final CartService cartService;
    private final UserService userService;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public CustomErrorController(BrandService brandService,
                                 CategoryService categoryService,
                                 CartService cartService,
                                 UserService userService,
                                 MessageSource messageSource,
                                 LocaleResolver localeResolver) {
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.cartService = cartService;
        this.userService = userService;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @GetMapping("/error")
    public String showErrorPage(HttpServletRequest request,
                                Authentication authentication,
                                Model model) {

        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("pageTitle", messageSource.getMessage(
                        "pageTitle.error.404",
                        null,
                        localeResolver.resolveLocale(request)));
                return "error-404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("pageTitle", messageSource.getMessage(
                        "pageTitle.error.500",
                        null,
                        localeResolver.resolveLocale(request)));
                return "error-500";
            }
        }

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.error",
                null,
                localeResolver.resolveLocale(request)));

        return "error";
    }
}
