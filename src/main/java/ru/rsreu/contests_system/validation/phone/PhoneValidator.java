package ru.rsreu.contests_system.validation.phone;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    @Value("${validation.phone.regex}")
    private static String regex;
    private static final Pattern PATTERN = Pattern.compile(regex);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return PATTERN.matcher(value).matches();
    }
}
