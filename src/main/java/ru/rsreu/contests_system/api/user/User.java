package ru.rsreu.contests_system.api.user;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.EnumSet;
import java.util.List;

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

    private EnumSet<Authority> authorities;

    private String password;

    private String educationPlace;

    private List<String> refreshTokens;

    private String confirmationToken;
}
