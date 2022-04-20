package ru.rsreu.contests_system.api.organization.event;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.task.Task;
import ru.rsreu.contests_system.api.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Event {
    @MongoId
    @Builder.Default private ObjectId id = new ObjectId();

    @Indexed(unique = true)
    private String name;

    private String description;

    private EventType eventType;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private long timeLimit;

    @DBRef
    @Indexed(unique = true)
    private User eventLeader;

    @DBRef
    @Builder.Default private Set<User> eventOrganizers = new HashSet<>();

    @DBRef
    @Builder.Default private Set<Task> tasks = new HashSet<>();

    @Builder.Default private Set<ParticipantInfo> participantsInfos = new HashSet<>();

    public void addEventOrganizer(User organizer) {
        eventOrganizers.add(organizer);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void addParticipantInfo(ParticipantInfo participantInfo) {
        participantsInfos.add(participantInfo);
    }
}
