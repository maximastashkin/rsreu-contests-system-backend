package ru.rsreu.contests_system.organization.event;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ru.rsreu.contests_system.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.task.Task;
import ru.rsreu.contests_system.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Event {
    @MongoId
    private ObjectId id;

    private EventType eventType;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @DBRef
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
