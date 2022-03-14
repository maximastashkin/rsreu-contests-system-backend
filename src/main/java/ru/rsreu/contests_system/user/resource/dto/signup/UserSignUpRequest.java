package ru.rsreu.contests_system.user.resource.dto.signup;

public record UserSignUpRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        String middleName,
        String educationPlace) {
}
