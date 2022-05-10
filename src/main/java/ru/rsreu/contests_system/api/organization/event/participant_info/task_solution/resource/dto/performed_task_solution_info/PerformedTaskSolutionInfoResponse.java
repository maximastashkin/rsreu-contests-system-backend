package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.performed_task_solution_info;

import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ExecutionStatus;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ProgrammingLanguage;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.test_info.ParticipantTestInfoResponse;

import java.util.List;

public record PerformedTaskSolutionInfoResponse(
        String taskTitle,
        String taskText,
        long memoryLimit,
        long timeLimit,
        String solutionText,
        ProgrammingLanguage language,
        ExecutionStatus executionStatus,
        String errorOutput,
        List<ParticipantTestInfoResponse> publicTests) {
}
