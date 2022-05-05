package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.completed_task_solution_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.performed_task_solution_info.PerformedTaskSolutionInfoMapper;

@Component
public record CompletedTaskSolutionInfoMapper(PerformedTaskSolutionInfoMapper taskSolutionInfoMapper) {
    public CompletedTaskSolutionInfoResponse toResponse(TaskSolution taskSolution) {
        return new CompletedTaskSolutionInfoResponse(
                taskSolutionInfoMapper.toResponse(taskSolution),
                taskSolution.calculateScore(),
                taskSolution.getTask().calculateMaxScore()
        );
    }
}
