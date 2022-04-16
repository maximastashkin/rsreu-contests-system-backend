package ru.rsreu.contests_system.api.organization;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Document(collection = "organizations")
public class Organization {
    public static final int ORGANIZERS_LIMIT = 10;

    @MongoId
    private ObjectId id;

    @Indexed(unique = true)
    private String name;

    @Builder.Default
    private String description = "";

    @Builder.Default
    private String pictureUrl = "";

    @Indexed(unique = true)
    private String organizationEmail;

    @Indexed(unique = true)
    private String organizationPhone;

    @DBRef
    private User organizationLeader;

    @DBRef
    @Builder.Default
    private Set<User> organizers = new HashSet<>();

    @Builder.Default
    private Set<Event> events = new HashSet<>();
}
