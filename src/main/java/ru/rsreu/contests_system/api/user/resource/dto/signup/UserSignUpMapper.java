package ru.rsreu.contests_system.api.user.resource.dto.signup;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.user.Authority;
import ru.rsreu.contests_system.api.user.User;

import java.util.EnumSet;

@Component
public record UserSignUpMapper(PasswordEncoder passwordEncoder) {
    public User toUser(UserSignUpRequest userSignUpRequest) {
        return User.builder()
                .firstName(userSignUpRequest.firstName().trim())
                .lastName(userSignUpRequest.lastName().trim())
                .middleName(userSignUpRequest.middleName().trim())
                .educationPlace(userSignUpRequest.educationPlace().trim())
                .password(passwordEncoder.encode(userSignUpRequest.password()))
                .email(userSignUpRequest.email().trim())
                .authorities(EnumSet.of(Authority.PARTICIPANT, Authority.UNBLOCKED, Authority.INACTIVE))
                .build();
    }
}