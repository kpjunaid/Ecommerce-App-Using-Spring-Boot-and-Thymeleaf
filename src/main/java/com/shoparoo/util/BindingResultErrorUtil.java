package com.shoparoo.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class BindingResultErrorUtil {
    public static ResponseEntity<Object> getBindingResultErrorResponse(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error: bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
