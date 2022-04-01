package ru.rsreu.contests_system.api.user.resource.dto.signup;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.user.BlockedStatus;
import ru.rsreu.contests_system.api.user.Role;
import ru.rsreu.contests_system.api.user.ActivationStatus;
import ru.rsreu.contests_system.api.user.User;

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
                .role(Role.PARTICIPANT)
                .activationStatus(ActivationStatus.INACTIVE)
                .blockedStatus(BlockedStatus.UNBLOCKED)
                .build();
    }
}
