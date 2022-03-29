package ru.rsreu.contests_system.api.organization_creating_application.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.rsreu.contests_system.api.organization_creating_application.OrganizationCreatingApplication;

import java.util.Optional;

public interface OrganizationCreatingApplicationRepository
        extends MongoRepository<OrganizationCreatingApplication, ObjectId> {
    Optional<OrganizationCreatingApplication> findByOrganizationEmail(String organizationEmail);

    Optional<OrganizationCreatingApplication> findByPhone(String phone);

    Optional<OrganizationCreatingApplication> findByLeaderEmail(String leaderEmail);
}
