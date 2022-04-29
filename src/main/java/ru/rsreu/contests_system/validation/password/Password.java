package ru.rsreu.contests_system.validation.password;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
    @Value("${validation.password.message}")
    String defaultMessage = "";
    String message() default defaultMessage;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
