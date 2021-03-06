package ru.rsreu.contests_system.validation.password;

import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    @Value("${validation.password.regexp}")
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
