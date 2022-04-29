package ru.rsreu.contests_system.validation.phone;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    @Value("${validation.phone.message}")
    String defaultMessage = "";
    String message() default "Bad phone";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
