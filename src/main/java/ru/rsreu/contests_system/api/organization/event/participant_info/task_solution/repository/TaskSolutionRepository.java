package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.repository;

import org.bson.types.ObjectId;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.user.User;

import java.util.Optional;

public interface TaskSolutionRepository {
    Optional<TaskSolution> findParticipantTaskSolutionById(User participant, ObjectId taskSolutionId);

    void setTaskSolutionCheckingInfo(TaskSolution taskSolution);

    void setTaskSolutionCheckingResultInfo(TaskSolution taskSolution);
}
