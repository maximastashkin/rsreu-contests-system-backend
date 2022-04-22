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

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/orgs/events")
@AllArgsConstructor
public class EventResource {
    private final EventService eventService;
    private final EventInfoMapper eventInfoMapper;

    @Operation(summary = "${api.orgs.events.all-actual.operation}")
    @GetMapping(value = "/all-actual/{pageSize}/{pageNumber}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.orgs.events.all-actual.response-codes.ok}"),
            @ApiResponse(responseCode = "400", description = "${api.orgs.events.all-actual.response-codes.bad-request}",
                    content = {@Content()}),
            @ApiResponse(responseCode = "401",
                    description = "${api.orgs.events.all-actual.response-codes.unauthorized}")
    })
    public ResponseEntity<List<EventInfoResponse>> getAllActualEvents(Authentication authentication,
                                                                      @PathVariable @Min(1) int pageSize,
                                                                      @Parameter(description = "${api.pageable_numbering.message}")
                                                                      @PathVariable @Min(0) int pageNumber) {
        return new ResponseEntity<>(eventService.getAllActualEvents(pageSize, pageNumber).stream().map(event ->
                eventInfoMapper.toResponse(event, authentication)).toList(),
                authentication != null ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }
}
