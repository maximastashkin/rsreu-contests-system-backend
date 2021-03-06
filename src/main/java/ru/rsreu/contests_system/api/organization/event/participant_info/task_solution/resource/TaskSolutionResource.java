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
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.completed_task_solution_info.CompletedTaskSolutionInfoMapper;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.completed_task_solution_info.CompletedTaskSolutionInfoResponse;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.performed_task_solution_info.PerformedTaskSolutionInfoMapper;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.performed_task_solution_info.PerformedTaskSolutionInfoResponse;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.supported_languages.SupportedLanguagesMapper;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.supported_languages.SupportedLanguagesResponse;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.task_checking.TaskCheckingRequest;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.TaskSolutionService;
import ru.rsreu.contests_system.api.organization.event.service.checking.ParticipantCompletingEventConditionChecker;
import ru.rsreu.contests_system.api.organization.event.service.checking.ParticipantPerformingEventConditionChecker;
import ru.rsreu.contests_system.validation.object_id.ObjectId;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/orgs/events/tasks")
@AllArgsConstructor
public class TaskSolutionResource {
    private final TaskSolutionService taskSolutionService;
    private final PerformedTaskSolutionInfoMapper performedTaskSolutionInfoMapper;
    private final CompletedTaskSolutionInfoMapper completedTaskSolutionInfoMapper;
    private final ParticipantPerformingEventConditionChecker performingEventConditionChecker;
    private final ParticipantCompletingEventConditionChecker completingEventConditionChecker;
    private final SupportedLanguagesMapper supportedLanguagesMapper;

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
                    description = "${api.orgs.events.tasks.performed.response-codes.conflict}",
                    content = @Content),
            @ApiResponse(responseCode = "410", description = "${api.orgs.events.tasks.performed.response-codes.gone}",
                    content = @Content)
    })
    public ResponseEntity<PerformedTaskSolutionInfoResponse> getStartedTaskInfo(
            Authentication authentication, @RequestParam @ObjectId String id) {
        return new ResponseEntity<>(
                performedTaskSolutionInfoMapper.toResponse(
                        taskSolutionService.getTaskSolutionByConditionChecking(
                                authentication, performingEventConditionChecker, id)),
                HttpStatus.OK);
    }

    @Operation(summary = "${api.orgs.events.tasks.check.operation}")
    @PostMapping(value = "/check", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.orgs.events.tasks.check.response-codes.ok}",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "${api.orgs.events.tasks.check.response-codes.bad-request}",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "${api.orgs.events.tasks.check.response-codes.not-found}",
                    content = @Content),
            @ApiResponse(responseCode = "406",
                    description = "${api.orgs.events.tasks.check.response-codes.not-acceptable}",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "${api.orgs.events.tasks.check.response-codes.conflict}",
                    content = @Content),
            @ApiResponse(responseCode = "410", description = "${api.orgs.events.tasks.check.response-codes.gone}",
                    content = @Content),
            @ApiResponse(responseCode = "503",
                    description = "${api.orgs.events.tasks.check.response-codes.service-unavailable}",
                    content = @Content)
    })
    public ResponseEntity<?> checkTask(
            Authentication authentication,
            @RequestParam @ObjectId String id,
            @RequestBody @Valid TaskCheckingRequest taskCheckingRequest) {
        taskSolutionService.checkServiceAlive();
        TaskSolution taskSolution = taskSolutionService
                .prepareTaskSolutionForChecking(authentication, id, taskCheckingRequest);
        taskSolutionService.asyncPerformCheckingTaskSolution(taskSolution);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "${api.orgs.events.tasks.completed.operation}")
    @GetMapping(value = "/completed", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.orgs.events.tasks.completed.response-codes.ok}"),
            @ApiResponse(responseCode = "400",
                    description = "${api.orgs.events.tasks.completed.response-codes.bad-request}",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "${api.orgs.events.tasks.completed.response-codes.not-found}",
                    content = @Content),
            @ApiResponse(responseCode = "406",
                    description = "${api.orgs.events.tasks.completed.response-codes.not-acceptable}",
                    content = @Content),
            @ApiResponse(responseCode = "409",
                    description = "${api.orgs.events.tasks.completed.response-codes.conflict}",
                    content = @Content)
    })
    public ResponseEntity<CompletedTaskSolutionInfoResponse> getCompletedTaskInfo(Authentication authentication,
                                                                                  @RequestParam @ObjectId String id) {
        return new ResponseEntity<>(
                completedTaskSolutionInfoMapper
                        .toResponse(
                                taskSolutionService.getTaskSolutionByConditionChecking(
                                        authentication, completingEventConditionChecker, id)),
                HttpStatus.OK);
    }

    @Operation(summary = "${api.orgs.events.tasks.supported-languages.operation}")
    @GetMapping(value = "/supported-languages", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "${api.orgs.events.tasks.supported-languages.response-codes.ok}")
    public ResponseEntity<List<SupportedLanguagesResponse>> getSupportedLanguages() {
        return new ResponseEntity<>(
                Arrays.stream(taskSolutionService.getAllSupportedLanguage())
                        .map(supportedLanguagesMapper::toResponse).toList(),
                HttpStatus.OK
        );
    }
}
