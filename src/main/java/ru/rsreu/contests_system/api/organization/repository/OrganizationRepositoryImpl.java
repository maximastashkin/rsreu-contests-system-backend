package ru.rsreu.contests_system.api.organization.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.util.RepositoryUtil;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationCustomRepository {
    private final MongoTemplate mongoTemplate;
    private final RepositoryUtil repositoryUtil;
    private final OrganizationRepositoryUtil organizationRepositoryUtil;

    @Override
    public void addOrganizerToOrganization(Organization organization, User organizer) {
        mongoTemplate.updateFirst(
                organizationRepositoryUtil.getOrganizationByIdQuery(organization),
                organizationRepositoryUtil.getUpdateForAddingOrganizerToOrganization(organizer),
                Organization.class
        );
    }

    public Optional<Organization> findOrganizationByOrganizer(User organizer) {
        List<Organization> results = mongoTemplate
                .find(organizationRepositoryUtil.getOrganizationByOrganizerQuery(organizer), Organization.class);
        return repositoryUtil.getOptionalByQueryResults(results);
    }

    @Override
    public void addEventToOrganization(Organization organization, Event event) {
        mongoTemplate.updateFirst(
                organizationRepositoryUtil.getOrganizationByIdQuery(organization),
                organizationRepositoryUtil.getUpdateForAddingEvent(event),
                Organization.class
        );
    }
}