package ru.rsreu.contests_system.api.organization.repository;

import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;

public interface OrganizationCustomRepository {
    void addOrganizerToOrganization(Organization organization, User organizer);

    void addEventToOrganization(Organization organization, Event event);
}
