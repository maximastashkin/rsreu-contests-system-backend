package ru.rsreu.contests_system.user.resource.dto.signup;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.user.Role;
import ru.rsreu.contests_system.user.Status;
import ru.rsreu.contests_system.user.User;

@Component
public record UserSignUpMapper(PasswordEncoder passwordEncoder) {

    public UserSignUpResponse toResponse(User user) {
        //TODO Tokens!!!
        return new UserSignUpResponse(null, null, user.getRole());
    }

    public User toUser(UserSignUpRequest userSignUpRequest) {
        return User.builder()
                .firstName(userSignUpRequest.firstName())
                .lastName(userSignUpRequest.lastName())
                .middleName(userSignUpRequest.middleName())
                .educationPlace(userSignUpRequest.educationPlace())
                .password(passwordEncoder.encode(userSignUpRequest.password()))
                .email(userSignUpRequest.email())
                .role(Role.PARTICIPANT)
                .status(Status.UN_ACTIVE)
                .build();
    }
}
