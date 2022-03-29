package ru.rsreu.contests_system.api.organization_creating_application.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.OrganizationCreatingApplicationRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/applications")
public class OrganizationCreatingApplicationResource {
    public ResponseEntity<?> createOrganizationCreatingApplication(
            @RequestBody @Valid OrganizationCreatingApplicationRequest request) {
        return null;
    }
}
