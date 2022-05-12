package ru.rsreu.contests_system.api.user.resource.dto.change_info;

import ru.rsreu.contests_system.validation.null_or_not_blank.NullOrNotBlank;

import javax.validation.constraints.NotBlank;

public record ChangeUserInfoRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NullOrNotBlank
        String middleName,
        @NullOrNotBlank
        String educationPlace) {
}
