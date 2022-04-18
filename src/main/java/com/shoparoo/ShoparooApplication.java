package com.shoparoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@SpringBootApplication
@EntityScan({"com.shoparoo.entity"})
public class ShoparooApplication implements WebMvcConfigurer {
	public static void main(String[] args) {
		SpringApplication.run(ShoparooApplication.class, args);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		localeChangeInterceptor.setIgnoreInvalidLocale(true);
		registry.addInterceptor(localeChangeInterceptor);
	}
}
