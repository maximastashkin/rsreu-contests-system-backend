package ru.rsreu.contests_system.api.organization_creating_application.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.application_creating.OrganizationCreatingApplicationMapper;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.application_creating.OrganizationCreatingApplicationRequest;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.application_info.OrganizationCreatingApplicationInfoMapper;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.application_info.OrganizationCreatingApplicationsInfoResponse;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_lead_email.CheckLeaderEmailUniqueMapper;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_lead_email.CheckLeaderEmailUniqueResponse;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_org_email.CheckOrganizationEmailUniqueMapper;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_org_email.CheckOrganizationEmailUniqueResponse;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_phone.CheckOrganizationPhoneUniqueMapper;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_phone.CheckOrganizationPhoneUniqueResponse;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.not_unique_info.NotUniqueOrganizationInfoResponse;
import ru.rsreu.contests_system.api.organization_creating_application.service.OrganizationCreatingApplicationService;
import ru.rsreu.contests_system.validation.object_id.ObjectId;
import ru.rsreu.contests_system.validation.phone.Phone;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@AllArgsConstructor
@Validated
public class OrganizationCreatingApplicationResource {
    private final OrganizationCreatingApplicationInfoMapper organizationCreatingApplicationInfoMapper;
    private final OrganizationCreatingApplicationMapper organizationCreatingApplicationMapper;
    private final CheckOrganizationPhoneUniqueMapper checkOrganizationPhoneUniqueMapper;
    private final CheckOrganizationEmailUniqueMapper checkOrganizationEmailUniqueMapper;
    private final CheckLeaderEmailUniqueMapper checkLeaderEmailUniqueMapper;
    private final OrganizationCreatingApplicationService organizationCreatingApplicationService;

    @Operation(summary = "${api.applications.approve.operation}")
    @PostMapping("/approve")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "${api.applications.approve.response-codes.created}",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "${api.applications.approve.response-codes.bad-request}",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NotUniqueOrganizationInfoResponse.class)
                            )
                    }),
            @ApiResponse(responseCode = "404", description = "${api.application.approve.response-codes.not-found}",
                    content = @Content)
    })
    public ResponseEntity<?> approveOrganizationCreatingApplication(@RequestParam @ObjectId String id) {
        //TODO EMAIL Sending
        organizationCreatingApplicationService.approveOrganizationCreatingApplication(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "${api.application.decline.operation}")
    @PostMapping("/decline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.application.decline.response-codes.ok}",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "${api.application.decline.response-codes.bad-request}",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "${api.application.decline.response-codes.not-found}",
                    content = @Content),
    })
    public ResponseEntity<?> declineOrganizationCreatingApplication(@RequestParam @ObjectId String id) {
        //TODO EMAIL Sending
        organizationCreatingApplicationService.declineOrganizationCreatingApplication(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "${api.applications.all.operation}")
    @GetMapping(path = "/{pageSize}/{pageNumber}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.applications.all.response-codes.ok}"),
            @ApiResponse(responseCode = "400", description = "${api.applications.all.response-codes.bad-request}",
                    content = @Content)
    })
    public ResponseEntity<List<OrganizationCreatingApplicationsInfoResponse>> getAllOrganizationCreatingApplication(
            @PathVariable @Min(1) int pageSize,
            @Parameter(description = "${api.pageable_numbering.message}")
            @PathVariable @Min(0) int pageNumber) {
        return new ResponseEntity<>(
                organizationCreatingApplicationService
                        .getAll(pageSize, pageNumber)
                        .stream()
                        .map(organizationCreatingApplicationInfoMapper::toResponse).toList(),
                HttpStatus.OK);
    }

    @Operation(summary = "${api.applications.creating.operation}")
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.applications.creating.response-codes.ok}",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "${api.applications.creating.response-codes.bad-request}",
                    content = @Content),
    })
    public ResponseEntity<?> createOrganizationCreatingApplication(
            @RequestBody @Valid OrganizationCreatingApplicationRequest request) {
        organizationCreatingApplicationService
                .save(organizationCreatingApplicationMapper.toOrganizationCreatingApplication(request));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "${api.applications.check-organization-email.operation}")
    @GetMapping(path = "/check-organization-email", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
                    "${api.applications.check-organization-email.response-codes.ok}"),
            @ApiResponse(responseCode = "400",
                    description = "${api.applications.check-organization-email.response-codes.bad-request}",
                    content = @Content)
    })
    public ResponseEntity<CheckOrganizationEmailUniqueResponse> checkOrganizationEmailUnique(
            @RequestParam @NotBlank @Email String email) {
        return new ResponseEntity<>(
                checkOrganizationEmailUniqueMapper.toResponse(
                        organizationCreatingApplicationService.isOrganizationEmailUnique(email)),
                HttpStatus.OK);
    }

    @Operation(summary = "${api.applications.check-organization-phone.operation}")
    @GetMapping(path = "/check-organization-phone", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
                    "${api.applications.check-organization-phone.response-codes.ok}"),
            @ApiResponse(responseCode = "400",
                    description = "${api.applications.check-organization-phone.response-codes.bad-request}",
                    content = @Content)
    })
    public ResponseEntity<CheckOrganizationPhoneUniqueResponse> checkOrganizationPhoneUnique(
            @RequestParam @NotBlank @Phone String phone) {
        return new ResponseEntity<>(
                checkOrganizationPhoneUniqueMapper.toResponse(
                        organizationCreatingApplicationService.isOrganizationPhoneUnique(phone)),
                HttpStatus.OK
        );
    }

    @Operation(summary = "${api.applications.check-leader-email.operation}")
    @GetMapping(path = "/check-leader-email", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
                    "${api.applications.check-leader-email.response-codes.ok}"),
            @ApiResponse(responseCode = "400",
                    description = "${api.applications.check-leader-email.response-codes.bad-request}",
                    content = @Content)
    })
    public ResponseEntity<CheckLeaderEmailUniqueResponse> checkLeaderEmailUnique(
            @RequestParam @NotBlank @Email String email) {
        return new ResponseEntity<>(
                checkLeaderEmailUniqueMapper.toResponse(
                        organizationCreatingApplicationService.isOrganizationLeaderEmailUnique(email)),
                HttpStatus.OK
        );
    }
}