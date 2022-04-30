package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.performed_task_solution_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.test_info.ParticipantTestInfoResponseMapper;

@Component
public record PerformedTaskSolutionInfoResponseMapper(
        ParticipantTestInfoResponseMapper participantTestInfoResponseMapper) {
    public PerformedTaskSolutionInfoResponse toResponse(TaskSolution taskSolution) {
        return new PerformedTaskSolutionInfoResponse(
                taskSolution.getTask().getText(),
                taskSolution.getTask().getMemoryLimitKb(),
                taskSolution.getTask().getTimeLimitMs(),
                taskSolution.getSolution(),
                taskSolution.getProgrammingLanguage(),
                taskSolution.getExecutionStatus(),
                taskSolution.getTestsInfos().stream().filter(
                                testInfo -> testInfo.getTaskTest().isPublic())
                        .map(participantTestInfoResponseMapper::toResponse).toList()
        );
    }
}
