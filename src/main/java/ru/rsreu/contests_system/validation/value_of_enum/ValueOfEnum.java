package ru.rsreu.contests_system.validation.value_of_enum;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface ValueOfEnum {
    @Value("${validation.enum.message}")
    String defaultMessage = "";
    Class<? extends Enum<?>> enumClass();
    String message() default defaultMessage;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}