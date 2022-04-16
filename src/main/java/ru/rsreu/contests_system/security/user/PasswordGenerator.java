package ru.rsreu.contests_system.security.user;

import com.github.curiousoddman.rgxgen.RgxGen;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {
    public String generatePassword() {
        return new RgxGen("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$").generate().substring(0, 7);
    }
}