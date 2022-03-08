package ru.rsreu.contests_system.user;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.EnumSet;

@Data
@Document(collection = "users")
public class User {
    @MongoId
    private ObjectId id;

    private String firstName;

    private String lastName;

    private String middleName;

    @Indexed(unique = true)
    private String email;

    private EnumSet<Role> roles;

    private String password;

    private String educationPlace;

    public User(String firstName, String lastName, String middleName, String email, EnumSet<Role> roles, String password, String educationPlace) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.educationPlace = educationPlace;
    }
}
