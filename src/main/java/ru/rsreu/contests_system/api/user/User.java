package ru.rsreu.contests_system.api.user;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@Document(collection = "users")
public class User {
    @MongoId
    private ObjectId id;

    private String firstName;

    private String lastName;

    @Builder.Default
    private String middleName = "";

    @Indexed(unique = true)
    private String email;

    private EnumSet<Authority> authorities;

    private String password;

    @Builder.Default
    private String educationPlace = "";

    private List<String> refreshTokens;

    private String confirmationToken;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
