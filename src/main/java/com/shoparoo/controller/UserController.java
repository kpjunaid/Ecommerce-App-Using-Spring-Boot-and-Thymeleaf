package com.shoparoo.controller;

import com.shoparoo.dto.AddressDto;
import com.shoparoo.dto.AdminUserUpdateDto;
import com.shoparoo.dto.UserDto;
import com.shoparoo.dto.UserUpdateDto;
import com.shoparoo.entity.Address;
import com.shoparoo.entity.User;
import com.shoparoo.exception.*;
import com.shoparoo.group.UserEmailGroup;
import com.shoparoo.group.UserPasswordGroup;
import com.shoparoo.group.UserSignupGroup;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class UserController {
    public static final int CONTENT_PER_PAGE = 10;
    private final UserService userService;
    private final AddressService addressService;
    private final CountryService countryService;
    private final RoleService roleService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final CartService cartService;
    private final MapStructMapper mapStructMapper;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public UserController(UserService userService,
                          AddressService addressService,
                          CountryService countryService,
                          RoleService roleService,
                          CategoryService categoryService,
                          BrandService brandService,
                          CartService cartService,
                          MapStructMapper mapStructMapper,
                          MessageSource messageSource,
                          LocaleResolver localeResolver) {
        this.userService = userService;
        this.addressService = addressService;
        this.countryService = countryService;
        this.roleService = roleService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.cartService = cartService;
        this.mapStructMapper = mapStructMapper;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @GetMapping("/signup")
    public String showSignupPage(HttpServletRequest request,
                                 Authentication authentication,
                                 Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);

        model.addAttribute("pageTitle", messageSource.getMessage(
                        "pageTitle.signup",
                        null,
                        localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "user-signup";
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signupNewUser(@Validated(UserSignupGroup.class) UserDto userDto,
                                                BindingResult bindingResult,
                                                HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return BindingResultErrorUtil.getBindingResultErrorResponse(bindingResult);
        }

        try {
            User savedUser = userService.saveNewUser(userDto);
            if (savedUser != null) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.signupSuccess",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.OK);
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {            e.printStackTrace();
            if (e instanceof EmailExistsException) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.exception.emailExistsException",
                        null,
                        localeResolver.resolveLocale(request)),
                        HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.serverError",
                        null,
                        localeResolver.resolveLocale(request)),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request,
                                Authentication authentication,
                                Model model) {
        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.login",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "user-login";
    }

    @GetMapping("/profile")
    public String showUserProfilePage(HttpServletRequest request,
                                      Authentication authentication,
                                      Model model) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            UserDto userDto = mapStructMapper.userToUserDto(user);
            model.addAttribute("user", userDto);

            try {
                Address address = addressService.getAddressById(userDto.getAddress().getId());
                AddressDto addressDto = mapStructMapper.addressToAddressDto(address);
                model.addAttribute("address", addressDto);
            } catch (AddressNotFoundException e) {
                return "redirect:/error";
            }
        } catch (UserNotFoundException e) {
            return "redirect:/error";
        }

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.profile",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "user-profile";
    }

    @GetMapping("/profile/update/info")
    public String showUpdateUserPage(HttpServletRequest request,
                                     Authentication authentication,
                                     Model model) {
        try {
            User user = userService.getUserByEmail(authentication.getName());
            UserDto userDto = mapStructMapper.userToUserDto(user);
            model.addAttribute("user", userDto);
            userService.addAuthUserToModel(authentication, model);

            try {
                Address address = addressService.getAddressById(userDto.getAddress().getId());
                AddressDto addressDto = mapStructMapper.addressToAddressDto(address);
                model.addAttribute("address", addressDto);
            } catch (AddressNotFoundException e) {
                return "redirect:/error";
            }
        } catch (UserNotFoundException e) {
            return "redirect:/error";
        }

        model.addAttribute("countries", countryService.getAllCountries());

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.profile.update.info",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "user-update-info";
    }

    @PostMapping("/profile/update/info")
    public ResponseEntity<Object> saveUpdateUserInfo(@Valid UserUpdateDto userUpdateDto,
                                                     BindingResult bindingResult,
                                                     @RequestParam(value = "userPhoto", required = false) MultipartFile userPhoto,
                                                     HttpServletRequest request,
                                                     Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return BindingResultErrorUtil.getBindingResultErrorResponse(bindingResult);
        } else {
            try {
                userService.updateUserInfo(authentication.getName(), userUpdateDto, userPhoto);
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.userUpdateSuccess",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.OK);
            } catch (RuntimeException e) {
                e.printStackTrace();
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.serverError",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/profile/update/email")
    public String showUpdateEmailPage(HttpServletRequest request,
                                      Authentication authentication,
                                      Model model) {
        UserDto userDto = mapStructMapper.userToUserDto(userService.getUserByEmail(authentication.getName()));
        model.addAttribute("user", userDto);

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.profile.update.email",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "user-update-email";
    }

    @PostMapping("/profile/update/email")
    public ResponseEntity<Object> saveUpdateUserEmail(@Validated(UserEmailGroup.class) UserDto userDto,
                                                      BindingResult bindingResult,
                                                      HttpServletRequest request,
                                                      Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return BindingResultErrorUtil.getBindingResultErrorResponse(bindingResult);
        }

        try {
            userService.updateUserEmail(authentication.getName(), userDto);
        } catch (RuntimeException e) {
            if (e instanceof UpdateWithCurrentEmailException) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.exception.updateWithCurrentEmailException",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
            } else if (e instanceof EmailExistsException) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.exception.emailExistsException",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
            } else if (e instanceof WrongPasswordException) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.exception.wrongPasswordException",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.serverError",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>("Email updated successfully", HttpStatus.OK);
    }

    @GetMapping("/profile/update/password")
    public String showUpdatePasswordPage(HttpServletRequest request,
                                         Authentication authentication,
                                         Model model) {
        User user;
        try {
            user = userService.getUserByEmail(authentication.getName());
        } catch (UserNotFoundException e) {
            return "redirect:/error";
        }
        UserDto userDto = mapStructMapper.userToUserDto(user);
        model.addAttribute("user", userDto);

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.profile.update.password",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "user-update-password";
    }

    @PostMapping("/profile/update/password")
    public ResponseEntity<Object> saveUpdateUserPassword(@Validated(UserPasswordGroup.class) UserDto userDto,
                                                         BindingResult bindingResult,
                                                         @RequestParam("oldPassword") String oldPassword,
                                                         HttpServletRequest request,
                                                         Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return BindingResultErrorUtil.getBindingResultErrorResponse(bindingResult);
        }

        try {
            userService.updateUserPassword(authentication.getName(), userDto, oldPassword);
        } catch (RuntimeException e) {
            if (e instanceof UpdateWithCurrentPasswordException) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.exception.updateWithCurrentPasswordException",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
            }  else if (e instanceof WrongPasswordException) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.exception.wrongPasswordException",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.serverError",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
    }

    @GetMapping("/verify-email")
    public String verifyUserEmail(@RequestParam(value = "token", required = false) String token,
                                  HttpServletRequest request,
                                  Authentication authentication,
                                  Model model) {
        userService.addAuthUserToModel(authentication, model);

        if (!token.isEmpty()) {
            try {
                userService.verifyUserEmail(token);
                model.addAttribute("verified", true);
            } catch (RuntimeException e) {
                model.addAttribute("verified", false);
            }
        } else {
            model.addAttribute("verified", false);
        }

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.profile.email.verify",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "user-email-verify";
    }

    @PostMapping("/email-token")
    public ResponseEntity<Object> sendVerifyEmailToken(@RequestParam(value = "userId", required = false) Long id,
                                                       HttpServletRequest request) {
        if (id != null) {
            try {
                userService.sendVerifyEmailToken(id);
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.tokenSendSuccess",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.OK);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.serverError",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(messageSource.getMessage(
                    "response.invalidIdError",
                    null,
                    localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage(HttpServletRequest request,
                                         Authentication authentication,
                                         Model model) {
        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.profile.password.reset",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "user-forgot-password";
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> sendForgotPasswordMail(@Validated(UserEmailGroup.class) UserDto userDto,
                                                         BindingResult bindingResult,
                                                         HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return BindingResultErrorUtil.getBindingResultErrorResponse(bindingResult);
        }

        try {
            userService.sendResetPasswordMail(userDto.getEmail());
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(messageSource.getMessage(
                    "response.exception.userNotFoundExceptionEmail",
                    null,
                    localeResolver.resolveLocale(request)), HttpStatus.NOT_FOUND);
        };

        return new ResponseEntity<>(messageSource.getMessage(
                "response.tokenSendIfEmailExistSuccess",
                null,
                localeResolver.resolveLocale(request)), HttpStatus.OK);
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam(value = "token", required = false) String token,
                                        HttpServletRequest request,
                                        Authentication authentication,
                                        Model model) {
        userService.addAuthUserToModel(authentication, model);

        if (token.isEmpty()) {
            model.addAttribute("token", false);
        } else {
            try {
                userService.checkResetPasswordTokenValidity(token);
                model.addAttribute("token", token);
            } catch (RuntimeException e) {
                model.addAttribute("token", false);
            }
        }

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.profile.password.reset",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "user-reset-password";
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetUserPassword(@RequestParam(value = "token") String token,
                                                    @Validated(UserPasswordGroup.class) UserDto userDto,
                                                    BindingResult bindingResult,
                                                    HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return BindingResultErrorUtil.getBindingResultErrorResponse(bindingResult);
        }

        try {
            userService.resetUserPassword(token, userDto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(messageSource.getMessage(
                    "response.invalidTokenError",
                    null,
                    localeResolver.resolveLocale(request)), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(messageSource.getMessage(
                "response.passwordResetSuccess",
                null,
                localeResolver.resolveLocale(request)), HttpStatus.OK);
    }

    @GetMapping("/admin/users")
    public String adminListUsers() {
        return "redirect:/admin/users/page/1";
    }

    @GetMapping("/admin/users/page/{pageNumber}")
    public String adminListUsersPageable(@PathVariable("pageNumber") Integer pageNumber,
                                         @RequestParam("search") Optional<String> search,
                                         @RequestParam("key") Optional<String> key,
                                         @RequestParam("sort") Optional<String> sort,
                                         @RequestParam("dir") Optional<String> dir,
                                         @RequestParam("gender") Optional<String> gender,
                                         @RequestParam("enabled") Optional<Boolean> enabled,
                                         @RequestParam("role") Optional<String> role,
                                         HttpServletRequest request,
                                         Authentication authentication,
                                         Model model) {
        if (pageNumber == null || pageNumber < 1)  pageNumber = 1;

        StringBuilder requestParamString = new StringBuilder();
        requestParamString.append("?");

        if (sort.isEmpty()) {
            sort = Optional.of("id");
        } else {
            requestParamString.append("sort=").append(sort.get()).append("&");
        }

        if (dir.isEmpty()) {
            dir = Optional.of("ASC");
        } else {
            requestParamString.append("dir=").append(dir.get().toUpperCase()).append("&");
        }

        if (gender.isEmpty()) {
            gender = Optional.of("");
        } else {
            requestParamString.append("gender=").append(StringUtils.capitalize(gender.get())).append("&");
        }

        if (role.isEmpty()) {
            role = Optional.of("");
        } else {
            requestParamString.append("role=").append(StringUtils.capitalize(role.get())).append("&");
        }

        if (search.isEmpty()) {
            search = Optional.of("");
        } else {
            requestParamString.append("search=").append(search.get()).append("&");
        }

        if (key.isEmpty()) {
            key = Optional.of("");
        } else {
            requestParamString.append("key=").append(key.get()).append("&");
        }

        if (requestParamString.length() <= 1) { // no request parameter given, remove '?' character
            requestParamString.setLength(0);
        } else {
            // remove last '&' character
            requestParamString.setLength(requestParamString.length()-1);
        }

        Page<User> foundUserPage = userService.getAllUsersPageable(
                pageNumber,
                CONTENT_PER_PAGE,
                sort.get(),
                dir.get(),
                gender.get(),
                role.get(),
                enabled,
                search.get(),
                key.get()
        );

        List<User> foundUserList = foundUserPage.getContent();

        int currentPage = pageNumber;
        int pageCount = foundUserPage.getTotalPages();
        long userCount = foundUserPage.getTotalElements();

        if (currentPage > pageCount) {
            currentPage = pageCount;
            foundUserList = userService.getAllUsersPageable(
                    pageCount,
                    CONTENT_PER_PAGE,
                    sort.get(),
                    dir.get(),
                    gender.get(),
                    role.get(),
                    enabled,
                    search.get(),
                    key.get()
            ).getContent();
        }

        long startCount = (long) (currentPage - 1) * CONTENT_PER_PAGE + 1;
        long endCount = startCount + CONTENT_PER_PAGE - 1;

        if (endCount > userCount) {
            endCount = userCount;
        }

        model.addAttribute("users", foundUserList);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("contentPerPage", CONTENT_PER_PAGE);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("userCount", userCount);
        model.addAttribute("sort",
                StringUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(sort.get()), ' ')));
        model.addAttribute("dir", StringUtils.capitalize(dir.get()));
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("requestParamString", requestParamString.toString());

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.admin.user.list",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "admin-user-list";
    }

    @GetMapping("/admin/users/update/{userId}")
    public String adminShowUpdateUserPage(@PathVariable("userId") Long id,
                                          HttpServletRequest request,
                                          Authentication authentication,
                                          Model model) {
        try {
            User user = userService.getUserById(id);
            UserDto userDto = mapStructMapper.userToUserDto(user);
            model.addAttribute("user", userDto);

            try {
                Address address = addressService.getAddressById(userDto.getAddress().getId());
                AddressDto addressDto = mapStructMapper.addressToAddressDto(address);
                model.addAttribute("address", addressDto);
            } catch (AddressNotFoundException e) {
                return "redirect:/error";
            }
        } catch (UserNotFoundException e) {
            return "redirect:/error";
        }

        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("countries", countryService.getAllCountries());
        model.addAttribute("userUpdate", true);

        model.addAttribute("pageTitle", messageSource.getMessage(
                "pageTitle.admin.user.update",
                null,
                localeResolver.resolveLocale(request)));
        model.addAttribute("navCategories", categoryService.getAllLeafCategories());
        model.addAttribute("navBrands", brandService.getAllBrands());
        userService.addAuthUserToModel(authentication, model);
        cartService.addCartItemsToModel(authentication, model);

        return "admin-user-update";
    }

    @PostMapping("/admin/users/update")
    public ResponseEntity<Object> adminSaveUpdateUserInfo(@Valid AdminUserUpdateDto adminUserUpdateDto,
                                                          BindingResult bindingResult,
                                                          @RequestParam("userId") Long id,
                                                          @RequestParam(value = "userPhoto", required = false) MultipartFile userPhoto,
                                                          HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return BindingResultErrorUtil.getBindingResultErrorResponse(bindingResult);
        } else {
            try {
                userService.adminUpdateUser(id, adminUserUpdateDto, userPhoto);
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.userUpdateSuccess",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.OK);
            } catch (RuntimeException e) {
                e.printStackTrace();
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.serverError",
                        null,
                        localeResolver.resolveLocale(request)), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @PostMapping("/admin/users/delete")
    public ResponseEntity<String> adminDeleteUser(@RequestParam("userId") Long id,
                                                  HttpServletRequest request) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>(messageSource.getMessage(
                    "response.userDeleteSuccess",
                    null,
                    localeResolver.resolveLocale(request)), HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e instanceof UserNotFoundException) {
                return new ResponseEntity<>(messageSource.getMessage(
                        "response.exception.userNotFoundException",
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
