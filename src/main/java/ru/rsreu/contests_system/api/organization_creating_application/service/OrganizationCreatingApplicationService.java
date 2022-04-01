package ru.rsreu.contests_system.api.organization_creating_application.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.service.OrganizationService;
import ru.rsreu.contests_system.api.organization_creating_application.OrganizationCreatingApplication;
import ru.rsreu.contests_system.api.organization_creating_application.exception.NotUniqueOrganizationInfo;
import ru.rsreu.contests_system.api.organization_creating_application.repository.OrganizationCreatingApplicationRepository;
import ru.rsreu.contests_system.api.user.service.UserService;

import java.util.List;

@Service
public record OrganizationCreatingApplicationService(
        UserService userService,
        OrganizationCreatingApplicationRepository organizationCreatingApplicationRepository,
        OrganizationService organizationService) {
    public boolean isUniqueOrganizationEmail(String organizationEmail) {
        return organizationCreatingApplicationRepository.findByOrganizationEmail(organizationEmail).isEmpty();
    }

    public boolean isUniqueOrganizationPhone(String phone) {
        return organizationCreatingApplicationRepository.findByOrganizationPhone(phone).isEmpty();
    }

    public boolean isUniqueOrganizationLeaderEmail(String email) {
        return organizationCreatingApplicationRepository.findByLeaderEmail(email).isEmpty()
                && userService.isEmailUnique(email);
    }

    public OrganizationCreatingApplication save(OrganizationCreatingApplication organizationCreatingApplication) {
        try {
            return organizationCreatingApplicationRepository.save(organizationCreatingApplication);
        } catch (RuntimeException exception) {
            String message = getExceptionMessage(organizationCreatingApplication).toString();
            throw new NotUniqueOrganizationInfo(message);
        }
    }

    private StringBuilder getExceptionMessage(OrganizationCreatingApplication organizationCreatingApplication) {
        StringBuilder message = new StringBuilder();
        if (!isUniqueOrganizationEmail(organizationCreatingApplication.getOrganizationEmail())) {
            message.append("not unique organization email: ")
                    .append(organizationCreatingApplication.getOrganizationEmail())
                    .append("\n");
        }
        if (!isUniqueOrganizationPhone(organizationCreatingApplication.getOrganizationPhone())) {
            message.append("not unique organization phone: ")
                    .append(organizationCreatingApplication.getOrganizationPhone())
                    .append("\n");
        }
        if (!isUniqueOrganizationLeaderEmail(organizationCreatingApplication.getLeaderEmail())) {
            message.append("not unique organization leader email: ")
                    .append(organizationCreatingApplication.getLeaderEmail());
        }
        return message;
    }

    public List<OrganizationCreatingApplication> getAll(int pageSize, int pageNumber) {
        return organizationCreatingApplicationRepository
                .findAll(PageRequest.of(pageNumber, pageSize)).stream().toList();
    }
}
