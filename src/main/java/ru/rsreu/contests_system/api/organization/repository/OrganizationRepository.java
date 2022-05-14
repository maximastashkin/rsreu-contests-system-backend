package ru.rsreu.contests_system.api.organization.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.user.User;

import java.util.Optional;

public interface OrganizationRepository extends MongoRepository<Organization, ObjectId>, OrganizationCustomRepository {
    Optional<Organization> findOrganizationByName(String name);

    Optional<Organization> findOrganizationByOrganizationEmail(String organizationEmail);

    Optional<Organization> findOrganizationByOrganizationPhone(String organizationPhone);

    Optional<Organization> findOrganizationOrganizationById(ObjectId id);

    Page<Organization> findAll(Pageable pageable);

    Optional<Organization> findOrganizationByOrganizationLeader(User organizationLeader);

    @Query(value = "{'events.name': /^?0$/i}}", count = true)
    long countAllByEventName(String eventName);
}
