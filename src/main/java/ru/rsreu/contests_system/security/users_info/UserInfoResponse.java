package ru.rsreu.contests_system.security.users_info;

import ru.rsreu.contests_system.api.user.Authority;

import java.util.EnumSet;

public record UserInfoResponse(
        String id,
        String firstName,
        String lastName,
        String middleName,
        String email,
        EnumSet<Authority> authorities) {
}
