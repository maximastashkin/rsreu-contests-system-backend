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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rsreu.contests_system.api.organization.event.resource.dto.event_info.EventInfoMapper;
import ru.rsreu.contests_system.api.organization.event.resource.dto.event_info.EventInfoResponse;
import ru.rsreu.contests_system.api.organization.event.service.EventService;
import ru.rsreu.contests_system.api.organization.util.UserCandidateByAuthenticationProvider;
import ru.rsreu.contests_system.api.user.User;

import javax.validation.constraints.Min;
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
                    content = {@Content()}),
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
                    content = {@Content()}),
            @ApiResponse(responseCode = "404",
                    description = "${api.orgs.events.user-all-actual.response-codes.not-found}",
                    content = {@Content()})
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
                        eventInfoMapper.toResponse(event, candidateForMapping))
                .toList(),
                HttpStatus.OK);
    }
}
