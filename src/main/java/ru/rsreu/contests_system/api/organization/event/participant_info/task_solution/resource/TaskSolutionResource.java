package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.performed_task_solution_info.PerformedTaskSolutionInfoResponse;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.performed_task_solution_info.PerformedTaskSolutionInfoResponseMapper;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.task_checking.TaskCheckingRequest;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.TaskSolutionService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@Validated
@RequestMapping("/api/orgs/events/tasks")
@AllArgsConstructor
public class TaskSolutionResource {
    private final TaskSolutionService taskSolutionService;
    private final PerformedTaskSolutionInfoResponseMapper performedTaskSolutionInfoResponseMapper;

    @Operation(summary = "${api.orgs.events.tasks.performed.operation}")
    @GetMapping(value = "/performed", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.orgs.events.tasks.performed.response-codes.ok}"),
            @ApiResponse(responseCode = "400",
                    description = "${api.orgs.events.tasks.performed.response-codes.bad-request}",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "${api.orgs.events.tasks.performed.response-codes.not-found}",
                    content = @Content),
            @ApiResponse(responseCode = "406",
                    description = "${api.orgs.events.tasks.performed.response-codes.not-acceptable}",
                    content = @Content),
            @ApiResponse(responseCode = "409",
                    description = "${api.orgs.events.tasks.performed.response-code.conflict}",
                    content = @Content),
            @ApiResponse(responseCode = "410", description = "${api.orgs.events.tasks.performed.response-code.gone}",
                    content = @Content)
    })
    public ResponseEntity<PerformedTaskSolutionInfoResponse> getStartedTaskInfo(
            Authentication authentication, @RequestParam @NotBlank String id) {
        return new ResponseEntity<>(
                performedTaskSolutionInfoResponseMapper.toResponse(
                        taskSolutionService.getTaskSolutionByAuthenticationAndId(authentication, id)),
                HttpStatus.OK);
    }

    @PostMapping(value = "/check", consumes = "application/json")
    public ResponseEntity<?> checkTask(
            Authentication authentication,
            @RequestParam @NotBlank String id,
            @RequestBody @Valid TaskCheckingRequest taskCheckingRequest) {
        taskSolutionService.checkTask(authentication, id, taskCheckingRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}