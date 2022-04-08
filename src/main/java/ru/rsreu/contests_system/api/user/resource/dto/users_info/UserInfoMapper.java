package ru.rsreu.contests_system.api.user.resource.dto.users_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.user.User;

@Component
public class UserInfoMapper {
    public UserInfoResponse toResponse(User user) {
        return new UserInfoResponse(
                user.getId().toString(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getEmail(),
                user.getAuthorities());
    }
}
