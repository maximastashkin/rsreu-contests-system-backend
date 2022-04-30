package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.performed_task_solution_info.PerformedTaskSolutionInfoResponse;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.performed_task_solution_info.PerformedTaskSolutionInfoResponseMapper;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.TaskSolutionService;

import javax.validation.constraints.NotBlank;

@RestController
@Validated
@RequestMapping("/api/orgs/events/tasks")
@AllArgsConstructor
public class TaskSolutionResource {
    private final TaskSolutionService taskSolutionService;
    private final PerformedTaskSolutionInfoResponseMapper performedTaskSolutionInfoResponseMapper;

    @GetMapping("/performed")
    public ResponseEntity<PerformedTaskSolutionInfoResponse> getStartedTaskInfo(
            Authentication authentication, @RequestParam @NotBlank String id) {
        return new ResponseEntity<>(
                performedTaskSolutionInfoResponseMapper.toResponse(
                        taskSolutionService.getTaskSolutionByAuthenticationAndId(authentication, id)),
                HttpStatus.OK);
    }
}
