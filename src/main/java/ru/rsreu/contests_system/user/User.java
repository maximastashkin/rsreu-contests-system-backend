package ru.rsreu.contests_system.user;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@Document(collection = "users")
public class User {
    @MongoId
    private ObjectId id;

    private String firstName;

    private String lastName;

    private String middleName;

    @Indexed(unique = true)
    private String email;

    private Role role;

    private Status status;

    private String password;

    private String educationPlace;
}
