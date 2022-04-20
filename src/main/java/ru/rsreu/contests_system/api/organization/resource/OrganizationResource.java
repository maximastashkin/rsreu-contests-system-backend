package ru.rsreu.contests_system.api.organization.resource;

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
import ru.rsreu.contests_system.api.organization.resource.dto.organization.organization_info.OrganizationInfoMapper;
import ru.rsreu.contests_system.api.organization.resource.dto.organization.organization_info.OrganizationInfoResponse;
import ru.rsreu.contests_system.api.organization.service.OrganizationService;

import javax.validation.constraints.NotBlank;

@RestController
@Validated
@RequestMapping("/api/orgs")
@AllArgsConstructor
public class OrganizationResource {
    private final OrganizationService organizationService;
    private final OrganizationInfoMapper organizationInfoMapper;

    @Operation(summary = "${api.orgs.info.operation}")
    @GetMapping(produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.orgs.response-codes.ok}"),
            @ApiResponse(responseCode = "400", description = "${api.orgs.response-codes.bad-request}",
                    content = {@Content()}),
            @ApiResponse(responseCode = "404", description = "${api.orgs.response-codes.not-found}",
                    content = {@Content()})
    })
    public ResponseEntity<OrganizationInfoResponse> getOrganization(Authentication authentication,
                                                                    @RequestParam @NotBlank String id) {
        return new ResponseEntity<>(
                organizationInfoMapper.toResponse(organizationService.getOrganizationById(id), authentication),
                HttpStatus.OK
        );
    }

    @PostMapping("/test")
    public ResponseEntity<?> addTestOrganizationWithEvents() {
        organizationService.addTestOrganization();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
