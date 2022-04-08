package ru.rsreu.contests_system.api.organization.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.user.User;

import java.util.Optional;

public interface OrganizationRepository extends MongoRepository<Organization, ObjectId> {
    Optional<Organization> findByOrganizationEmail(String organizationEmail);

    Optional<Organization> findByOrganizationPhone(String organizationPhone);

    Optional<Organization> findOrganizationById(ObjectId id);
}
