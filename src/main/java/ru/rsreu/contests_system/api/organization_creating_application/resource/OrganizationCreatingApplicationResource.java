package ru.rsreu.contests_system.api.organization_creating_application.resource;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_lead_email.CheckLeaderEmailUniqueMapper;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_lead_email.CheckLeaderEmailUniqueResponse;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_org_email.CheckOrganizationEmailUniqueMapper;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_org_email.CheckOrganizationEmailUniqueResponse;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_phone.CheckOrganizationPhoneUniqueMapper;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_phone.CheckOrganizationPhoneUniqueResponse;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.creating.OrganizationCreatingApplicationMapper;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.creating.OrganizationCreatingApplicationRequest;
import ru.rsreu.contests_system.api.organization_creating_application.service.OrganizationCreatingApplicationService;
import ru.rsreu.contests_system.validation.phone.Phone;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/applications")
@AllArgsConstructor
@Validated
public class OrganizationCreatingApplicationResource {
    private final OrganizationCreatingApplicationMapper organizationCreatingApplicationMapper;
    private final CheckOrganizationPhoneUniqueMapper checkOrganizationPhoneUniqueMapper;
    private final CheckOrganizationEmailUniqueMapper checkOrganizationEmailUniqueMapper;
    private final OrganizationCreatingApplicationService organizationCreatingApplicationService;
    private final CheckLeaderEmailUniqueMapper checkLeaderEmailUniqueMapper;

    @PostMapping
    public ResponseEntity<?> createOrganizationCreatingApplication(
            @RequestBody @Valid OrganizationCreatingApplicationRequest request) {
        organizationCreatingApplicationService
                .save(organizationCreatingApplicationMapper.toOrganizationCreatingApplication(request));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "${api.applications.check-organization-email.operation}")
    @GetMapping("/check-organization-email")
    public ResponseEntity<CheckOrganizationEmailUniqueResponse> checkOrganizationEmailUnique(
            @RequestParam @NotBlank @Email String email) {
        return new ResponseEntity<>(
                        checkOrganizationEmailUniqueMapper.toResponse(
                                organizationCreatingApplicationService.isUniqueOrganizationEmail(email)),
                        HttpStatus.OK);
    }

    @Operation(summary = "${api.applications.check-organization-phone.operation}")
    @GetMapping("/check-organization-phone")
    public ResponseEntity<CheckOrganizationPhoneUniqueResponse> checkOrganizationPhoneUnique(
            @RequestParam @NotBlank @Phone String phone) {
        return new ResponseEntity<>(
                checkOrganizationPhoneUniqueMapper.toResponse(
                        organizationCreatingApplicationService.isUniqueOrganizationPhone(phone)),
                        HttpStatus.OK
                );
    }

    @Operation(summary = "${api.applications.check-leader-email.operation}")
    @GetMapping("/check-leader-email")
    public ResponseEntity<CheckLeaderEmailUniqueResponse> checkLeaderEmailUnique(
            @RequestParam @NotBlank @Email String email) {
        return new ResponseEntity<>(
                checkLeaderEmailUniqueMapper.toResponse(
                        organizationCreatingApplicationService.isUniqueOrganizationLeaderEmail(email)),
                HttpStatus.OK
        );
    }
}