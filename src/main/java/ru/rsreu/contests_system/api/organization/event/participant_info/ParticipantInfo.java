package ru.rsreu.contests_system.api.organization.event.participant_info;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ru.rsreu.contests_system.api.organization.event.participant_info.appeal.Appeal;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class ParticipantInfo {
    @MongoId
    @Builder.Default private ObjectId objectId = new ObjectId();

    @DBRef
    @Indexed(unique = true)
    private User participant;

    private Appeal appeal;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean completed;

    @Builder.Default private Set<TaskSolution> tasksSolutions = new HashSet<>();
}
