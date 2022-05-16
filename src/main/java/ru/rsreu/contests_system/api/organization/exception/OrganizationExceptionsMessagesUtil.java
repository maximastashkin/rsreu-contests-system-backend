package ru.rsreu.contests_system.api.organization.exception;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;

@Component
public class OrganizationExceptionsMessagesUtil {
    public String formOrganizationNotFoundByIdExceptionMessage(String organizationId) {
        return String.format("Organization with id: %s didn't found", organizationId);
    }

    public String formOrganizationNotFoundByLeaderExceptionMessage(User leader) {
        return String.format("Organization for leader with email:%s didn't find", leader.getEmail());
    }

    public String formOrganizationNotFoundByOrganizerExceptionMessage(User organizer) {
        return String.format("Organization for organizer with id:%s didn't find", organizer.getId().toString());
    }

    public String formOrganizationNotFoundByEventExceptionMessage(Event event) {
        return String.format("Organization for event with id:%s didn't find", event.getId().toString());
    }

    public String formNotOrganizationLeaderExceptionMessage(Organization organization, User user) {
        return String.format("User with id:%s isn't leader of organization with id:%s",
                user.getId().toString(), organization.getId().toString());
    }
}
