package ru.rsreu.contests_system.api.organization.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.exception.OrganizationNotFoundException;
import ru.rsreu.contests_system.api.organization.repository.OrganizationRepository;

import java.util.NoSuchElementException;

@Service
public record OrganizationService(
        OrganizationRepository organizationRepository) {
    public boolean isEmailUnique(String email) {
        return organizationRepository.findByOrganizationEmail(email).isEmpty();
    }

    public boolean isPhoneUnique(String phone) {
        return organizationRepository.findByOrganizationPhone(phone).isEmpty();
    }

    public Organization getOrganizationById(String id) {
        return organizationRepository.findOrganizationById(id).orElseThrow(
                () -> new OrganizationNotFoundException(String.format("Organization with id: %s didn't found", id))
        );
    }
}
