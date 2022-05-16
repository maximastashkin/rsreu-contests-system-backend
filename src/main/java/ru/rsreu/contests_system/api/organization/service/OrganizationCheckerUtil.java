package ru.rsreu.contests_system.api.organization.service;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.exception.NotOrganizationLeaderException;
import ru.rsreu.contests_system.api.organization.exception.OrganizationExceptionsMessagesUtil;
import ru.rsreu.contests_system.api.user.User;

@Component
public record OrganizationCheckerUtil(OrganizationExceptionsMessagesUtil organizationExceptionsMessagesUtil) {
    public void checkOrganizationLeader(Organization organization, User candidate) {
        if (!organization.isLeader(candidate)) {
            throw new NotOrganizationLeaderException(organizationExceptionsMessagesUtil
                    .formNotOrganizationLeaderExceptionMessage(organization, candidate));
        }
    }
}
