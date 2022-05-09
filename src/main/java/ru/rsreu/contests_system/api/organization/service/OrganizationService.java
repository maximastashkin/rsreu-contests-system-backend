package ru.rsreu.contests_system.api.organization.service;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.exception.OrganizationExceptionMessageUtil;
import ru.rsreu.contests_system.api.organization.exception.OrganizationNotFoundException;
import ru.rsreu.contests_system.api.organization.repository.OrganizationRepository;
import ru.rsreu.contests_system.api.organization_creating_application.exception.NotUniqueOrganizationInfo;
import ru.rsreu.contests_system.api.organization_creating_application.exception.NotUniqueOrganizationInfoException;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.service.UserService;
import ru.rsreu.contests_system.security.user.PasswordGenerator;

import java.util.EnumSet;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
@Service
@AllArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserService userService;
    private final OrganizationExceptionMessageUtil organizationExceptionMessageUtil;
    private final PasswordGenerator passwordGenerator;

    public void save(Organization organization) {
        EnumSet<NotUniqueOrganizationInfo> exceptionInfo = getNotUniqueOrganizationInfo(organization);
        try {
            organizationRepository.save(organization);
        } catch (RuntimeException exception) {
            throw new NotUniqueOrganizationInfoException(exceptionInfo);
        }
    }

    private EnumSet<NotUniqueOrganizationInfo> getNotUniqueOrganizationInfo(Organization organization) {
        EnumSet<NotUniqueOrganizationInfo> info = EnumSet.noneOf(NotUniqueOrganizationInfo.class);
        if (!isNameUnique(organization.getName())) {
            info.add(NotUniqueOrganizationInfo.ORG_NAME);
        }
        if (!isEmailUnique(organization.getOrganizationEmail())) {
            info.add(NotUniqueOrganizationInfo.ORG_EMAIL);
        }
        if (!isPhoneUnique(organization.getOrganizationPhone())) {
            info.add(NotUniqueOrganizationInfo.ORG_PHONE);
        }
        return info;
    }

    public boolean isNameUnique(String name) {
        return organizationRepository.findByName(name).isEmpty();
    }

    public boolean isEmailUnique(String email) {
        return organizationRepository.findByOrganizationEmail(email).isEmpty();
    }

    public boolean isPhoneUnique(String phone) {
        return organizationRepository.findByOrganizationPhone(phone).isEmpty();
    }

    public Organization getOrganizationById(String id) {
        return organizationRepository.findOrganizationById(new ObjectId(id)).orElseThrow(
                () -> new OrganizationNotFoundException(organizationExceptionMessageUtil
                        .formOrganizationNotFoundException(id))
        );
    }

    public List<Organization> getAll(int pageSize, int pageNumber) {
        return organizationRepository.findAll(PageRequest.of(pageNumber, pageSize)).stream().toList();
    }

    @Transactional
    public void addOrganizer(Authentication authentication, User organizer) {
        //TODO Email sending
        String password = passwordGenerator.generatePassword();
        organizer.setPassword(password);
        organizer = userService.save(organizer);
        Organization organization = getOrganizationByLeader(
                userService.getUserByAuthentication(authentication));
        organizationRepository.addOrganizerToOrganization(organization, organizer);
    }

    private Organization getOrganizationByLeader(User organizationLeader) {
        return organizationRepository.findOrganizationByOrganizationLeader(organizationLeader).orElseThrow(
                () -> new OrganizationNotFoundException(organizationExceptionMessageUtil
                        .formOrganizationNotFoundException(organizationLeader))
        );
    }
}
