package ru.rsreu.contests_system.security.user;

import com.github.curiousoddman.rgxgen.RgxGen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {
    @Value("${security.password_generation.regexp}")
    private String passwordGenerationRegex;

    public String generatePassword() {
        RgxGen rgxGen = new RgxGen(passwordGenerationRegex);
        return rgxGen.generate().substring(0, 17);
    }
}