package ru.rsreu.contests_system.validation.object_id;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
@Component
public class NotRequiredObjectIdValidator implements ConstraintValidator<NotRequiredObjectId, String> {
    private final ObjectIdValidator objectIdValidator;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || objectIdValidator.isValid(value, context);
    }
}
