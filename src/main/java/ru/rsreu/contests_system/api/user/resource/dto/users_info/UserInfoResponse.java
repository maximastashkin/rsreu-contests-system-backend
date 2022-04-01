package ru.rsreu.contests_system.api.user.resource.dto.users_info;

import ru.rsreu.contests_system.api.user.Role;
import ru.rsreu.contests_system.api.user.ActivationStatus;

public record UserInfoResponse(
        String id,
        String firstName,
        String lastName,
        String middleName,
        String email,
        Role role,
        ActivationStatus activationStatus) {
}