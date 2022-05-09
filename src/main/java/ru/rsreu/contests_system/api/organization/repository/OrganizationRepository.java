package ru.rsreu.contests_system.api.organization.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.user.User;

import java.util.Optional;

public interface OrganizationRepository extends MongoRepository<Organization, ObjectId>, OrganizationCustomRepository {
    Optional<Organization> findByName(String name);

    Optional<Organization> findByOrganizationEmail(String organizationEmail);

    Optional<Organization> findByOrganizationPhone(String organizationPhone);

    Optional<Organization> findOrganizationById(ObjectId id);

    Page<Organization> findAll(Pageable pageable);

    Optional<Organization> findOrganizationByOrganizationLeader(User organizationLeader);
}
