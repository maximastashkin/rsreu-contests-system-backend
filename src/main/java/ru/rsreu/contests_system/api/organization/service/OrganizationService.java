package ru.rsreu.contests_system.api.organization.service;

import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.repository.OrganizationRepository;

@Service
public record OrganizationService(
        OrganizationRepository organizationRepository) {
    public boolean isEmailUnique(String email) {
        return organizationRepository.findByOrganizationEmail(email).isEmpty();
    }

    public boolean isPhoneUnique(String phone) {
        return organizationRepository.findByOrganizationPhone(phone).isEmpty();
    }
}
