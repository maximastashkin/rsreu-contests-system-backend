package ru.rsreu.contests_system.organization;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.rsreu.contests_system.user.User;

public interface OrganizationRepository extends MongoRepository<Organization, ObjectId> {
    Organization findOrganizationByOrganizationLeader(User leader);
}
