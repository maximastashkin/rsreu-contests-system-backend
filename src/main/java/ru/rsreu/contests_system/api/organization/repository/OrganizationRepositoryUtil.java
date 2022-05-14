package ru.rsreu.contests_system.api.organization.repository;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class OrganizationRepositoryUtil {
    public Query getOrganizationByOrganizerQuery(User organizer) {
        return Query.query(where("organizers.$id").is(organizer.getId()));
    }

    public Update getUpdateForAddingEvent(Event event) {
        return new Update().push("events").value(event);
    }

    public Query getOrganizationByIdQuery(Organization organization) {
        return Query.query(where("_id").is(organization.getId()));
    }

    public Update getUpdateForAddingOrganizerToOrganization(User organizer) {
        return new Update().push("organizers").value(organizer);
    }
}
