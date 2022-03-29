package ru.rsreu.contests_system.user.resource.dto.signup;

import ru.rsreu.contests_system.validation.null_or_not_blank.NullOrNotBlank;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record UserSignUpRequest(
        @NotBlank @Email(message = "Email isn't valid")
        String email,
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
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
