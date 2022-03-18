package ru.rsreu.contests_system.security.refresh.black_list;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenBlackListRepository extends MongoRepository<RefreshToken, ObjectId> {
}
