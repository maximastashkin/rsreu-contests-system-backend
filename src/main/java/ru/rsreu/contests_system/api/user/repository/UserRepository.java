package ru.rsreu.contests_system.api.user.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.rsreu.contests_system.api.user.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId>, UserCustomRepository {
    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Optional<User> findByConfirmationToken(String confirmationToken);
}
