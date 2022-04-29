package ru.rsreu.contests_system.api.organization.resource;

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
import ru.rsreu.contests_system.api.organization.resource.dto.organization.organization_info.OrganizationInfoMapper;
import ru.rsreu.contests_system.api.organization.resource.dto.organization.organization_info.OrganizationInfoResponse;
import ru.rsreu.contests_system.api.organization.resource.dto.organizations_info.OrganizationsInfoMapper;
import ru.rsreu.contests_system.api.organization.resource.dto.organizations_info.OrganizationsInfoResponse;
import ru.rsreu.contests_system.api.organization.service.OrganizationService;
import ru.rsreu.contests_system.api.organization.util.UserCandidateByAuthenticationProvider;
import ru.rsreu.contests_system.api.user.User;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/orgs")
@AllArgsConstructor
public class OrganizationResource {
    private final OrganizationService organizationService;
    private final OrganizationInfoMapper organizationInfoMapper;
    private final OrganizationsInfoMapper organizationsInfoMapper;
    private final UserCandidateByAuthenticationProvider userCandidateByAuthenticationProvider;

    @Operation(summary = "${api.orgs.info.operation}")
    @GetMapping(produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.orgs.response-codes.ok}"),
            @ApiResponse(responseCode = "400", description = "${api.orgs.response-codes.bad-request}",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "${api.orgs.response-codes.unauthorized}"),
            @ApiResponse(responseCode = "404", description = "${api.orgs.response-codes.not-found}",
                    content = @Content)
    })
    public ResponseEntity<OrganizationInfoResponse> getOrganization(Authentication authentication,
                                                                    @RequestParam @NotBlank String id) {
        Optional<User> candidateForMapping =
                userCandidateByAuthenticationProvider.getCandidateForMapping(authentication);
        return new ResponseEntity<>(
                organizationInfoMapper.toResponse(organizationService.getOrganizationById(id),
                        userCandidateByAuthenticationProvider.getCandidateForMapping(authentication)),
                candidateForMapping.isPresent() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED
        );
    }

    @Operation(summary = "${api.orgs.all.operation}")
    @GetMapping(path = "/all/{pageSize}/{pageNumber}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.orgs.all.response-codes.ok}"),
            @ApiResponse(responseCode = "400", description = "${api.orgs.all.response-codes.bad-request}",
                    content = @Content)
    })
    public ResponseEntity<List<OrganizationsInfoResponse>> getAllOrganizationsInfo(
            @PathVariable @Min(1) int pageSize,
            @Parameter(description = "${api.pageable_numbering.message}")
            @PathVariable @Min(0) int pageNumber) {
        return new ResponseEntity<>(
                organizationService
                        .getAll(pageSize, pageNumber)
                        .stream().map(organizationsInfoMapper::toResponse).toList(),
                HttpStatus.OK);
    }
}
