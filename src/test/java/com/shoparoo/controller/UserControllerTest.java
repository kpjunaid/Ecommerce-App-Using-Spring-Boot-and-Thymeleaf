//package com.shoparoo.controller;
//
//import com.shoparoo.mapper.MapStructMapper;
//import com.shoparoo.repository.UserRepository;
//import com.shoparoo.service.*;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.MessageSource;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.servlet.LocaleResolver;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(UserController.class)
//class UserControllerTest {
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    UserService userService;
//
//    @MockBean
//    UserRepository userRepository;
//
//    @MockBean
//    UserDetailsService userDetailsService;
//
//    @MockBean
//    RoleService roleService;
//
//    @MockBean
//    AddressService addressService;
//
//    @MockBean
//    CountryService countryService;
//
//    @MockBean
//    BrandService brandService;
//
//    @MockBean
//    CategoryService categoryService;
//
//    @MockBean
//    CartService cartService;
//
//    @MockBean
//    MessageSource messageSource;
//
//    @MockBean
//    LocaleResolver localeResolver;
//
//    @MockBean
//    MapStructMapper mapStructMapper;
//
//    public static final Integer CONTENT_PER_PAGE = 10;
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void shouldShowSignupPage() throws Exception {
//        mockMvc
//                .perform(get("/signup"))
//                .andExpect(status().isOk());
//    }
//}