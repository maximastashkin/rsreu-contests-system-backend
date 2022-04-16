package ru.rsreu.contests_system.validation.null_or_not_blank;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {
    String message() default "Bad null or blank field";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
