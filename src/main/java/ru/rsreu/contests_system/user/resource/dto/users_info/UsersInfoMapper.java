package ru.rsreu.contests_system.user.resource.dto.users_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.user.User;

@Component
public class UsersInfoMapper {
    public UsersInfoResponse toResponse(User user) {
        return new UsersInfoResponse(
                user.getId().toString(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }
}
