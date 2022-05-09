package ru.rsreu.contests_system.api.organization.resource.dto.organizer_adding;

import ru.rsreu.contests_system.validation.null_or_not_blank.NullOrNotBlank;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record OrganizerAddingRequest(
        @Email
        String email,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NullOrNotBlank
        String middleName) {
}
