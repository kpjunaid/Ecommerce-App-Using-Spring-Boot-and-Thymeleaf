package com.shoparoo.annotation;

import com.shoparoo.util.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Password cannot be empty, it must be alphanumeric," +
            "contains both uppercase and lowercase and length must be between 6 to 32 characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
