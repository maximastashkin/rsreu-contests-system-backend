package ru.rsreu.contests_system.user;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "users")
public class User {
    @MongoId
    @Generated
    private ObjectId id;

    @Indexed(unique = true)
    private String name;

    @Indexed(unique = true)
    private String email;

    private Role role;

    private String password;

    private String educationPlace;

    public User(String name, String email, Role role, String password, String educationPlace) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
        this.educationPlace = educationPlace;
    }
}
