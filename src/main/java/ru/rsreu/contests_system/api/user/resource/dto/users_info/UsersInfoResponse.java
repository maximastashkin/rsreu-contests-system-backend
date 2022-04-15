package ru.rsreu.contests_system.api.user.resource.dto.users_info;

import ru.rsreu.contests_system.api.user.Authority;

import java.util.EnumSet;

public record UsersInfoResponse(
        String id,
        String firstName,
        String lastName,
        String middleName,
        String email,
        EnumSet<Authority> authorities) {
}
