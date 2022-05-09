package ru.rsreu.contests_system.api.organization.exception;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.user.User;

@Component
public class OrganizationExceptionMessageUtil {
    public String formOrganizationNotFoundException(String organizationId) {
        return String.format("Organization with id: %s didn't found", organizationId);
    }

    public String formOrganizationNotFoundException(User leader) {
       return String.format("Organization for leader with email:%s didn't find", leader.getEmail());
    }
}
