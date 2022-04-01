package ru.rsreu.contests_system.validation.phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    Pattern pattern = Pattern.compile("(^8|7|\\+7)((\\d{10})|(\\s\\(\\d{3}\\)\\s\\d{3}\\s\\d{2}\\s\\d{2}))");
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return pattern.matcher(value).matches();
    }
}
