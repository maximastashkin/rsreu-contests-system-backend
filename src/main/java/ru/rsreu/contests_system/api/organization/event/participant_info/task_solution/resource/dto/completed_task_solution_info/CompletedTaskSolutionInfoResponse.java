package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.completed_task_solution_info;

import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.performed_task_solution_info.PerformedTaskSolutionInfoResponse;

public record CompletedTaskSolutionInfoResponse(
        PerformedTaskSolutionInfoResponse taskSolutionInfo,
        int score,
        int maxScore) {
}
