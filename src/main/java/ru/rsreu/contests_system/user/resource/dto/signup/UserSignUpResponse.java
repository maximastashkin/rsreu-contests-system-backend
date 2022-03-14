package ru.rsreu.contests_system.user.resource.dto.signup;

import ru.rsreu.contests_system.user.Role;

public record UserSignUpResponse(String token, String refreshToken,
                                 Role role) {
}
