package ru.rsreu.contests_system.api.organization.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.contests_system.api.organization.resource.dto.organization_info.OrganizationInfoMapper;
import ru.rsreu.contests_system.api.organization.resource.dto.organization_info.OrganizationInfoResponse;
import ru.rsreu.contests_system.api.organization.service.OrganizationService;

import javax.validation.constraints.NotBlank;

@RestController
@Validated
@RequestMapping("/api/orgs")
@AllArgsConstructor
public class OrganizationResource {
    private final OrganizationService organizationService;
    private final OrganizationInfoMapper organizationInfoMapper;

    @Operation(summary = "${api.org.info.operation}")
    @GetMapping(produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.org.response-codes.ok}"),
            @ApiResponse(responseCode = "400", description = "${api.org.response-codes.bad-request}",
                    content = {@Content()}),
            @ApiResponse(responseCode = "404", description = "${api.org.response-codes.not-found}",
                    content = {@Content()})
    })
    public ResponseEntity<OrganizationInfoResponse> getOrganization(@RequestParam @NotBlank String id) {
        return new ResponseEntity<>(
                organizationInfoMapper.toResponse(organizationService.getOrganizationById(id)),
                HttpStatus.OK
        );
    }
}
