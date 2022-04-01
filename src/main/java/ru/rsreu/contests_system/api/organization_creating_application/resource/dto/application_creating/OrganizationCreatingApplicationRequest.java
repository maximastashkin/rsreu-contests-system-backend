package ru.rsreu.contests_system.api.organization_creating_application.resource.dto.application_creating;

import ru.rsreu.contests_system.validation.null_or_not_blank.NullOrNotBlank;
import ru.rsreu.contests_system.validation.phone.Phone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record OrganizationCreatingApplicationRequest(
        @NotBlank
        String name,
        @Email
        String organizationEmail,
        @NotBlank @Phone
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
