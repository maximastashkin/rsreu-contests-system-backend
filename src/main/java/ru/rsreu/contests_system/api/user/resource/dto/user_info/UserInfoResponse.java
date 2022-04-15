package ru.rsreu.contests_system.api.user.resource.dto.user_info;

public record UserInfoResponse(
        String email,
        String firstName,
        String lastName,
        String middleName,
        String educationPlace) {
}
