package ru.rsreu.contests_system.api.organization.event.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.contests_system.api.organization.event.resource.dto.event_info.EventInfoMapper;
import ru.rsreu.contests_system.api.organization.event.resource.dto.event_info.EventInfoResponse;
import ru.rsreu.contests_system.api.organization.event.service.EventService;
import ru.rsreu.contests_system.api.organization.util.UserCandidateByAuthenticationProvider;
import ru.rsreu.contests_system.api.user.User;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/orgs/events")
@AllArgsConstructor
public class EventResource {
    private final EventService eventService;
    private final EventInfoMapper eventInfoMapper;
    private final UserCandidateByAuthenticationProvider userCandidateByAuthenticationProvider;

    @Operation(summary = "${api.orgs.events.all-actual.operation}")
    @GetMapping(value = "/all-actual/{pageSize}/{pageNumber}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.orgs.events.all-actual.response-codes.ok}"),
            @ApiResponse(responseCode = "400", description = "${api.orgs.events.all-actual.response-codes.bad-request}",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "${api.orgs.events.all-actual.response-codes.unauthorized}")
    })
    public ResponseEntity<List<EventInfoResponse>> getAllActualEvents(
            Authentication authentication,
            @PathVariable @Min(1) int pageSize,
            @Parameter(description = "${api.pageable_numbering.message}")
            @PathVariable @Min(0) int pageNumber) {
        Optional<User> candidateForMapping =
                userCandidateByAuthenticationProvider.getCandidateForMapping(authentication);
        return new ResponseEntity<>(eventService.getAllActualEvents(pageSize, pageNumber).stream().map(event ->
                eventInfoMapper.toResponse(event, candidateForMapping)).toList(),
                candidateForMapping.isPresent() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }

    @Operation(summary = "${api.orgs.events.user-all-actual.operation}")
    @GetMapping(value = "/all-actual/user/{pageSize}/{pageNumber}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.orgs.events.user-all-actual.response-codes.ok}"),
            @ApiResponse(responseCode = "400",
                    description = "${api.orgs.events.user-all-actual.response-codes.bad-request}",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "${api.orgs.events.user-all-actual.response-codes.not-found}",
                    content = @Content)
    })
    public ResponseEntity<List<EventInfoResponse>> getAllActualEventsForUser(
            Authentication authentication,
            @PathVariable @Min(1) int pageSize,
            @Parameter(description = "${api.pageable_numbering.message}")
            @PathVariable @Min(0) int pageNumber) {
        Optional<User> candidateForMapping =
                userCandidateByAuthenticationProvider.getCandidateForMapping(authentication);
        return new ResponseEntity<>(eventService.getAllUserActualEvents(authentication, pageSize, pageNumber)
                .stream().map(event ->
                        eventInfoMapper.toResponse(event, candidateForMapping)).toList(),
                HttpStatus.OK);
    }

    @Operation(summary = "${api.orgs.events.user-all-completed.operation}")
    @GetMapping(value = "/all-completed/user/{pageSize}/{pageNumber}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.orgs.events.user-all-completed.response-codes.ok}"),
            @ApiResponse(responseCode = "400",
                    description = "${api.orgs.events.user-all-completed.response-codes.bad-request}",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "${api.orgs.events.user-all-completed.response-codes.not-found}", content = @Content)
    })
    public ResponseEntity<List<EventInfoResponse>> getAllCompletedEventsForUser(
            Authentication authentication,
            @PathVariable @Min(1) int pageSize,
            @Parameter(description = "${api.pageable_numbering.message}")
            @PathVariable @Min(0) int pageNumber) {
        Optional<User> candidateForMapping =
                userCandidateByAuthenticationProvider.getCandidateForMapping(authentication);
        return new ResponseEntity<>(eventService.getAllUserCompletedEvents(authentication, pageSize, pageNumber)
                .stream().map(event ->
                        eventInfoMapper.toResponse(event, candidateForMapping)).toList(),
                HttpStatus.OK);
    }

    @Operation(summary = "${api.orgs.events.follow.operation}")
    @PostMapping(value = "/follow")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "${api.orgs.events.follow.response-codes.created}",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "${api.orgs.events.follow.response-codes.bad-request}",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "${api.orgs.events.follow.response-codes.not-found}",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "${api.orgs.events.follow.response-codes.conflict}",
                    content = @Content),
            @ApiResponse(responseCode = "410", description = "${api.orgs.events.follow.response-codes.gone}",
                    content = @Content)
    })
    public ResponseEntity<?> followToEvent(Authentication authentication, @RequestParam @NotBlank String id) {
        eventService.followToEvent(authentication, id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "${api.orgs.events.unfollow.operation}")
    @PostMapping(value = "/unfollow")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.orgs.events.unfollow.response-codes.ok}",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "${api.orgs.events.unfollow.response-codes.bad-request}",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "${api.orgs.events.unfollow.response-codes.not-found}",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "${api.orgs.events.unfollow.response-codes.conflict}",
                    content = @Content),
            @ApiResponse(responseCode = "410", description = "${api.orgs.events.unfollow.response-codes.gone}",
                    content = @Content)
    })
    public ResponseEntity<?> unfollowFromEvent(Authentication authentication, @RequestParam @NotBlank String id) {
        eventService.unfollowFromEvent(authentication, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "${api.orgs.events.start.operation}")
    @PostMapping(value = "/start")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.orgs.events.start.response-codes.ok}",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "${api.orgs.events.start.response-codes.bad-request}",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "${api.orgs.events.start.response-codes.not-found}",
                    content = @Content),
            @ApiResponse(responseCode = "406", description = "${api.orgs.events.start.response-codes.not-acceptable}",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "${api.orgs.events.start.response-codes.conflict}",
                    content = @Content),
            @ApiResponse(responseCode = "410", description = "${api.orgs.events.start.response-codes.gone}",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "${api.orgs.events.start.response-codes.internal-server-error}",
                    content = @Content)
    })
    public ResponseEntity<?> startEvent(Authentication authentication, @RequestParam @NotBlank String id) {
        eventService.startEvent(authentication, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
