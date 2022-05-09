package ru.rsreu.contests_system.api.organization.resource.dto.organizer_adding;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.user.Authority;
import ru.rsreu.contests_system.api.user.User;

import java.util.EnumSet;
import java.util.UUID;

@Component
public class OrganizerAddingMapper {
    public User toUser(OrganizerAddingRequest organizerAddingRequest) {
        return User.builder()
                .email(organizerAddingRequest.email().trim())
                .firstName(organizerAddingRequest.firstName().trim())
                .lastName(organizerAddingRequest.lastName().trim())
                .middleName(organizerAddingRequest.middleName() == null ? "" :
                        organizerAddingRequest.middleName().trim())
                .authorities(EnumSet.of(Authority.ORGANIZER, Authority.UNBLOCKED, Authority.INACTIVE))
                .confirmationToken(UUID.randomUUID().toString())
                .build();
    }
}
