package com.shoparoo.annotation;

import com.shoparoo.util.PasswordRepeatEqualValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordRepeatEqualValidator.class)
public @interface PasswordRepeatEqual {
    String message() default "Password did not match";
    String passwordFieldFirst();
    String passwordFieldSecond();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
