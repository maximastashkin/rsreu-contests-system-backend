package ru.rsreu.contests_system.validation.object_id;

import org.springframework.beans.factory.annotation.Value;
import ru.rsreu.contests_system.validation.null_or_not_blank.NullOrNotBlank;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NotRequiredObjectIdValidator.class)
@NullOrNotBlank
public @interface NotRequiredObjectId {
    @Value("${validation.not_required_object_id.message}")
    String defaultMessage = "";
    String message() default defaultMessage;
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
