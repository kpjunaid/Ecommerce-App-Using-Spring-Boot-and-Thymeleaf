package com.shoparoo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoparoo.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public SecurityConfig(UserRepository userRepository,
                          UserDetailsService userDetailsService,
                          ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/images/**", "/js/**", "/css/**", "/font/**", "/webjars/**", "/uploads/**").permitAll()
                    .antMatchers("/", "/products/**", "/error", "/signup", "/login", "/logout").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(this::loginSuccessHandler)
                    .failureHandler(this::loginFailureHandler)
                .and()
                    .rememberMe()
                    .key("secretKey")
                    .tokenValiditySeconds(14 * 24 * 60 * 60) // 2 weeks
                    .userDetailsService(userDetailsService)
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .logoutSuccessUrl("/login?logout=true")
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .invalidateHttpSession(true)
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                    .exceptionHandling();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall getHttpFirewall() {
        StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
        strictHttpFirewall.setAllowUrlEncodedDoubleSlash(true);
        return strictHttpFirewall;
    }

    private void loginSuccessHandler(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        Long userId = userRepository.findByEmail(user.getUsername()).get().getId();
        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getWriter(), userId);
    }

    private void loginFailureHandler(HttpServletRequest request,
                                     HttpServletResponse response,
                                     AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
    }
}
