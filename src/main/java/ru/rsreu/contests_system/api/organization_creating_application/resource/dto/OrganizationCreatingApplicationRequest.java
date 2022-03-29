package ru.rsreu.contests_system.api.organization_creating_application.resource.dto;

import ru.rsreu.contests_system.validation.null_or_not_blank.NullOrNotBlank;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record OrganizationCreatingApplicationRequest(
        @NotBlank
        String name,
        @Email
        String organizationEmail,
        @NotBlank
        String organizationPhone,
        @NotBlank
        String leaderFirstName,
        @NotBlank
        String leaderLastName,
        @NullOrNotBlank
        String leaderMiddleName,
        @Email
        String leaderEmail) {
}
