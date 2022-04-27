package ru.rsreu.contests_system.api.task.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.rsreu.contests_system.api.task.Task;

public interface TaskRepository extends MongoRepository<Task, ObjectId> {
}
