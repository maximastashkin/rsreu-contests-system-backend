package ru.rsreu.contests_system.api.organization.exception;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.user.User;

@Component
public class OrganizationExceptionsMessagesUtil {
    public String formOrganizationNotFoundByLeaderException(String organizationId) {
        return String.format("Organization with id: %s didn't found", organizationId);
    }

    public String formOrganizationNotFoundByLeaderException(User leader) {
       return String.format("Organization for leader with email:%s didn't find", leader.getEmail());
    }

    public String formOrganizationNotFoundByOrganizerException(User organizer) {
        return String.format("Organization for organizer with id:%s didn't find", organizer.getId().toString());
    }
}
