package ru.rsreu.contests_system.validation.object_id;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Component
public class ObjectIdValidator implements ConstraintValidator<ObjectId, String> {
    @Value("${validation.object_id.regexp}")
    private String regex;
    private Pattern pattern;

    @PostConstruct
    private void initPattern() {
        pattern = Pattern.compile(regex);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return pattern.matcher(value).matches();
    }
}
