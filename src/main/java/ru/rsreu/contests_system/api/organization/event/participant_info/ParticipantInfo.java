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
import java.util.Objects;
import java.util.Set;

@Data
@Builder
public class ParticipantInfo {
    @MongoId
    @Builder.Default
    private ObjectId id = new ObjectId();

    @DBRef
    @Indexed(unique = true)
    private User participant;

    private Appeal appeal;

    private LocalDateTime startDateTime;

    private LocalDateTime maxEndDateTime;

    private LocalDateTime factEndDateTime;

    @Builder.Default
    private Set<TaskSolution> tasksSolutions = new HashSet<>();

    public boolean isParticipantStartEvent() {
        return startDateTime != null;
    }

    public boolean isParticipantCompletedEvent() {
        return factEndDateTime != null;
    }

    public static ParticipantInfo getTaskSolutionForDeletingByParticipant(User participant) {
        return builder().id(null).participant(participant).tasksSolutions(null).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantInfo that = (ParticipantInfo) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
