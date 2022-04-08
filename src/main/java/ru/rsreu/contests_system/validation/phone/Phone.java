package ru.rsreu.contests_system.validation.phone;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    String message() default "{javax.validation.constraints.Phone.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
