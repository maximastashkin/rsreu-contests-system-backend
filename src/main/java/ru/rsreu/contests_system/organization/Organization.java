package ru.rsreu.contests_system.organization;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ru.rsreu.contests_system.organization.event.Event;
import ru.rsreu.contests_system.user.User;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Document(collection = "organizations")
public class Organization {
    public static final int ORGANIZERS_LIMIT = 10;

    @MongoId
    private ObjectId id;

    private String name;

    @DBRef
    private User organizationLeader;

    @DBRef
    @Builder.Default private Set<User> organizers = new HashSet<>();

    @Builder.Default private Set<Event> events = new HashSet<>();

    public void addOrganizer(User organizer) {
        organizers.add(organizer);
    }

    public void addEvent(Event event) {
        events.add(event);
    }
}
