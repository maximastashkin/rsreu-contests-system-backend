package ru.rsreu.contests_system.api.organization_creating_application.service;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.service.OrganizationService;
import ru.rsreu.contests_system.api.organization_creating_application.OrganizationCreatingApplication;
import ru.rsreu.contests_system.api.organization_creating_application.exception.NotFoundOrganizationCreatingApplicationException;
import ru.rsreu.contests_system.api.organization_creating_application.exception.NotUniqueOrganizationInfo;
import ru.rsreu.contests_system.api.organization_creating_application.exception.NotUniqueOrganizationInfoException;
import ru.rsreu.contests_system.api.organization_creating_application.repository.OrganizationCreatingApplicationRepository;
import ru.rsreu.contests_system.api.user.Authority;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.service.UserService;
import ru.rsreu.contests_system.security.user.PasswordGenerator;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ClassCanBeRecord")
@AllArgsConstructor
@Service
public class OrganizationCreatingApplicationService {
    private final UserService userService;
    private final OrganizationService organizationService;
    private final OrganizationCreatingApplicationRepository organizationCreatingApplicationRepository;
    private final PasswordGenerator passwordGenerator;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = RuntimeException.class)
    public void approveOrganizationCreatingApplication(String organizationId) {
        OrganizationCreatingApplication application = getApplicationById(organizationId);
        User leader = mapApplicationToUser(application);
        String password = passwordGenerator.generatePassword(); // TODO EMAIL sending
        leader.setPassword(passwordEncoder.encode(password));
        leader = userService.save(leader);
        organizationService.save(mapApplicationToOrganization(application, leader));
        organizationCreatingApplicationRepository.delete(application);
    }

    public void declineOrganizationCreatingApplication(String organizationId) {
        //TODO EMAIL sending
        OrganizationCreatingApplication application = getApplicationById(organizationId);
        organizationCreatingApplicationRepository.delete(application);
    }

    private OrganizationCreatingApplication getApplicationById(String organizationId) {
        return organizationCreatingApplicationRepository.findById(new ObjectId(organizationId)).orElseThrow(
                () -> new NotFoundOrganizationCreatingApplicationException(
                        String.format("Organization with id: %s didn't found", organizationId))
        );
    }

    private User mapApplicationToUser(OrganizationCreatingApplication application) {
        return User.builder()
                .firstName(application.getLeaderFirstName())
                .lastName(application.getLeaderLastName())
                .middleName(application.getLeaderMiddleName())
                .email(application.getLeaderEmail())
                .confirmationToken(UUID.randomUUID().toString())
                .authorities(EnumSet.of(Authority.ORGANIZATION_LEADER, Authority.UNBLOCKED, Authority.INACTIVE))
                .build();
    }

    private Organization mapApplicationToOrganization(OrganizationCreatingApplication application, User leader) {
        return Organization.builder()
                .name(application.getName())
                .organizationEmail(application.getOrganizationEmail())
                .organizationPhone(application.getOrganizationPhone())
                .organizationLeader(leader)
                .build();
    }

    public boolean isNameUnique(String name) {
        return organizationCreatingApplicationRepository.findByName(name).isEmpty() &&
                organizationService.isNameUnique(name);
    }

    public boolean isOrganizationEmailUnique(String email) {
        return organizationCreatingApplicationRepository.findByOrganizationEmail(email).isEmpty()
                && organizationService.isEmailUnique(email);
    }

    public boolean isOrganizationPhoneUnique(String phone) {
        return organizationCreatingApplicationRepository.findByOrganizationPhone(phone).isEmpty()
                && organizationService.isPhoneUnique(phone);
    }

    public boolean isOrganizationLeaderEmailUnique(String email) {
        return organizationCreatingApplicationRepository.findByLeaderEmail(email).isEmpty()
                && userService.isEmailUnique(email);
    }

    public void save(OrganizationCreatingApplication application) {
        try {
            organizationCreatingApplicationRepository.save(application);
        } catch (RuntimeException exception) {
            throw new NotUniqueOrganizationInfoException(getNotUniqueOrganizationInfo(application));
        }
    }

    private EnumSet<NotUniqueOrganizationInfo> getNotUniqueOrganizationInfo(OrganizationCreatingApplication application) {
        EnumSet<NotUniqueOrganizationInfo> info = EnumSet.noneOf(NotUniqueOrganizationInfo.class);
        if (!isNameUnique(application.getName())) {
            info.add(NotUniqueOrganizationInfo.ORG_NAME);
        }
        if (!isOrganizationEmailUnique(application.getOrganizationEmail())) {
            info.add(NotUniqueOrganizationInfo.ORG_EMAIL);
        }
        if (!isOrganizationPhoneUnique(application.getOrganizationPhone())) {
            info.add(NotUniqueOrganizationInfo.ORG_PHONE);
        }
        if (!isOrganizationLeaderEmailUnique(application.getLeaderEmail())) {
            info.add(NotUniqueOrganizationInfo.LEADER_EMAIL);
        }
        return info;
    }

    public List<OrganizationCreatingApplication> getAll(int pageSize, int pageNumber) {
        return organizationCreatingApplicationRepository
                .findAll(PageRequest.of(pageNumber, pageSize)).stream().toList();
    }
}
