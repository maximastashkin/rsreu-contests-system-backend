package ru.rsreu.contests_system.api.organization.service;

import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.organization.exception.OrganizationNotFoundException;
import ru.rsreu.contests_system.api.organization.repository.OrganizationRepository;
import ru.rsreu.contests_system.api.organization_creating_application.exception.NotUniqueOrganizationInfo;
import ru.rsreu.contests_system.api.organization_creating_application.exception.NotUniqueOrganizationInfoException;
import ru.rsreu.contests_system.api.user.User;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

@Service
public record OrganizationService(OrganizationRepository organizationRepository) {
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
        if (!isEmailUnique(organization.getOrganizationEmail())) {
            info.add(NotUniqueOrganizationInfo.ORG_EMAIL);
        }
        if (!isPhoneUnique(organization.getOrganizationPhone())) {
            info.add(NotUniqueOrganizationInfo.ORG_PHONE);
        }
        return info;
    }

    public boolean isEmailUnique(String email) {
        return organizationRepository.findByOrganizationEmail(email).isEmpty();
    }

    public boolean isPhoneUnique(String phone) {
        return organizationRepository.findByOrganizationPhone(phone).isEmpty();
    }

    public Organization getOrganizationById(String id) {
        return organizationRepository.findOrganizationById(new ObjectId(id)).orElseThrow(
                () -> new OrganizationNotFoundException(String.format("Organization with id: %s didn't found", id))
        );
    }

    public List<Organization> getAll(int pageSize, int pageNumber) {
        return organizationRepository.findAll(PageRequest.of(pageNumber, pageSize)).stream().toList();
	}
	
    public void addTestOrganization() {
        //TODO Delete this shit. Method test data provider
        Organization organization = Organization.builder()
                .name("Test organization")
                .build();
        Event firstEvent = Event.builder()
                .name("First test event")
                .startDateTime(LocalDateTime.of(2022, 4, 21, 10, 52, 0))
                .endDateTime(LocalDateTime.of(2022, 4, 25, 22, 0, 0))
                .build();
        Event secondEvent = Event.builder()
                .name("Second test event")
                .startDateTime(LocalDateTime.of(2022, 4, 15, 10, 30, 0))
                .endDateTime(LocalDateTime.of(2022, 4, 22, 22, 0, 0))
                .build();
        Event thirdEvent = Event.builder()
                .name("Third test event")
                .startDateTime(LocalDateTime.of(2022, 4, 15, 10, 30, 0))
                .endDateTime(LocalDateTime.of(2022, 4, 18, 22, 0, 0))
                .build();
        organization.getEvents().add(firstEvent);
        organization.getEvents().add(thirdEvent);

        User user = User.builder().id(new ObjectId("6263008a7d47a8013b335ea9")).email("test@mail.ru").build();

        Organization secondOrg = Organization.builder()
                .name("Second organization")
                .organizationEmail("test")
                .organizationPhone("test")
                .organizationLeader(User.builder().id(new ObjectId("625aeddb211d133f5d5bb39f")).build())
                .build();
        secondEvent.addParticipantInfo(ParticipantInfo.builder()
                .participant(user).completed(true).build());
        thirdEvent.addParticipantInfo(ParticipantInfo.builder().participant(user).build());
        firstEvent.addParticipantInfo(ParticipantInfo.builder().participant(user).build());
        secondOrg.getEvents().add(secondEvent);
        organizationRepository.save(organization);
        organizationRepository.save(secondOrg);
    }
}
