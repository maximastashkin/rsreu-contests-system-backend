package ru.rsreu.contests_system.api.organization.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.Event;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends MongoRepository<Organization, ObjectId>, OrganizationCustomRepository {
    Optional<Organization> findByOrganizationEmail(String organizationEmail);

    Optional<Organization> findByOrganizationPhone(String organizationPhone);

    Optional<Organization> findOrganizationById(ObjectId id);
}
