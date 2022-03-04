package ru.rsreu.contests_system.organization.event.participant_info;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ru.rsreu.contests_system.organization.event.participant_info.appeal.Appeal;
import ru.rsreu.contests_system.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.user.User;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class ParticipantInfo {
    @MongoId
    private ObjectId objectId;

    @DBRef
    private User participant;

    private Appeal appeal;

    @Builder.Default private Set<TaskSolution> tasksSolutions = new HashSet<>();

    public void addTaskSolution(TaskSolution taskSolution) {
        tasksSolutions.add(taskSolution);
    }
}
