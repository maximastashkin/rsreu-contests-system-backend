package ru.rsreu.contests_system.user.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.rsreu.contests_system.user.User;

import java.util.List;

//Here write default methods signatures. It will work without impl.
public interface UserRepository extends MongoRepository<User, ObjectId>, UserCustomRepository {
    List<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);
}
