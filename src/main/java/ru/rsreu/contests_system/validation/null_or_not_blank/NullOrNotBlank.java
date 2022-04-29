package ru.rsreu.contests_system.validation.null_or_not_blank;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {
    @Value("${validation.not-null-or-blank.message}")
    String defaultMessage = "";
    String message() default defaultMessage;
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
