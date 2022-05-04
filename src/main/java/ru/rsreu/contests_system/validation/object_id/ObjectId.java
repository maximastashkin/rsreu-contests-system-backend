package ru.rsreu.contests_system.validation.object_id;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ObjectIdValidator.class)
@NotBlank
public @interface ObjectId {
    @Value("${validation.object_id.message}")
    String defaultMessage = "";
    String message() default defaultMessage;
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
