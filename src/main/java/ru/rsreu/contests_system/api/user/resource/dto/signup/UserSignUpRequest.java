package ru.rsreu.contests_system.api.user.resource.dto.signup;

import ru.rsreu.contests_system.validation.null_or_not_blank.NullOrNotBlank;
import ru.rsreu.contests_system.validation.password.Password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record UserSignUpRequest(
        @NotBlank @Email(message = "Email isn't valid")
        String email,

        @Password
        String password,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NullOrNotBlank
        String middleName,

        @NotBlank
        String educationPlace) {
}