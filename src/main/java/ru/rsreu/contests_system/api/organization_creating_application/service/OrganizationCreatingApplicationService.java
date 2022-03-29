package ru.rsreu.contests_system.api.organization_creating_application.service;

import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.service.OrganizationService;
import ru.rsreu.contests_system.api.organization_creating_application.repository.OrganizationCreatingApplicationRepository;
import ru.rsreu.contests_system.api.user.service.UserService;

@Service
public record OrganizationCreatingApplicationService(
        OrganizationCreatingApplicationRepository organizationCreatingApplicationRepository,
        OrganizationService organizationService,
        UserService userService) {
    public boolean isUniqueOrganizationEmail(String organizationEmail) {
        return organizationCreatingApplicationRepository.findByOrganizationEmail(organizationEmail).isEmpty();
    }

    public boolean isUniquePhone(String phone) {
        return organizationCreatingApplicationRepository.findByPhone(phone).isEmpty();
    }

    public boolean isUniqueLeaderEmail(String leaderEmail) {
        return organizationCreatingApplicationRepository.findByLeaderEmail(leaderEmail).isEmpty() &&
                userService.isEmailUnique(leaderEmail);
    }
}
