package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.test_info;

import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ExecutionStatus;

public record ParticipantTestInfoResponse(
        String input,
        String output,
        ExecutionStatus executionStatus,
        String errorOutput,
        boolean isTestPassed) {
}
