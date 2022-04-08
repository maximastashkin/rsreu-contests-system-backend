package ru.rsreu.contests_system.api.organization.resource;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.contests_system.api.organization.resource.dto.organization_info.OrganizationInfoMapper;
import ru.rsreu.contests_system.api.organization.resource.dto.organization_info.OrganizationInfoResponse;
import ru.rsreu.contests_system.api.organization.service.OrganizationService;

import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/org")
public class OrganizationResource {

    private final OrganizationService organizationService;
    private final OrganizationInfoMapper organizationInfoMapper;

    @Autowired
    public OrganizationResource(
            OrganizationService organizationService,
            OrganizationInfoMapper organizationInfoMapper) {
        this.organizationService = organizationService;
        this.organizationInfoMapper = organizationInfoMapper;
    }


    @Operation(summary = "$api.org.info.operation")
    @GetMapping(path="/info", produces = "application/json")
    public ResponseEntity<OrganizationInfoResponse> getOrganization(@RequestParam String id) {
        System.out.println(id);
        return Optional.ofNullable(organizationService.getOrganizationById(id))
                .map(result -> new ResponseEntity<>(organizationInfoMapper
                                                        .toResponse(organizationService
                                                                        .getOrganizationById(id)),
                                                    HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
