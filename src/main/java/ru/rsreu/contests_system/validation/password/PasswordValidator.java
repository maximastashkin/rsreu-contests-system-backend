package ru.rsreu.contests_system.validation.password;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    @Value("${validation.password.regex}")
    private static String regex;
    private static final Pattern PATTERN = Pattern.compile(regex);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return PATTERN.matcher(value).matches();
    }
}
