package ru.rsreu.contests_system.user.resource.dto.users_info;

import ru.rsreu.contests_system.user.Role;
import ru.rsreu.contests_system.user.Status;

public record UsersInfoResponse(String id, String firstName, String lastName, String middleName, String email,
                                Role role, Status status) {
}
