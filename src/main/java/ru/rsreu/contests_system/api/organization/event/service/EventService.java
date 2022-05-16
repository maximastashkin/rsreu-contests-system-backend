package ru.rsreu.contests_system.api.organization.event.service;

import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.EventType;
import ru.rsreu.contests_system.api.organization.event.exception.EventExceptionsMessagesUtil;
import ru.rsreu.contests_system.api.organization.event.exception.EventNotFoundException;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.organization.event.participant_info.service.EventCompletingTasksHolder;
import ru.rsreu.contests_system.api.organization.event.participant_info.service.ParticipantInfoService;
import ru.rsreu.contests_system.api.organization.event.repository.EventRepository;
import ru.rsreu.contests_system.api.organization.event.resource.dto.event_creating.EventWithCreator;
import ru.rsreu.contests_system.api.organization.event.resource.dto.leader_changing.EventLeaderChangingRequest;
import ru.rsreu.contests_system.api.organization.event.service.checking.EventCheckerUtil;
import ru.rsreu.contests_system.api.organization.event.service.checking.ParticipantEventConditionChecker;
import ru.rsreu.contests_system.api.organization.exception.OrganizationNotFoundException;
import ru.rsreu.contests_system.api.organization.service.OrganizationCheckerUtil;
import ru.rsreu.contests_system.api.organization.service.OrganizationService;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public record EventService(
        OrganizationService organizationService,
        EventRepository eventRepository,
        ParticipantInfoService participantInfoService,
        UserService userService,
        EventExceptionsMessagesUtil eventExceptionsMessagesUtil,
        EventDateUtil eventDateUtil,
        EventCompletingTasksHolder tasksHolder,
        EventCheckerUtil eventCheckerUtil,
        OrganizationCheckerUtil organizationCheckerUtil) {
    public List<Event> getAllActualEvents(int pageSize, int pageNumber) {
        return eventRepository.findAllActualEvents(PageRequest.of(pageNumber, pageSize));
    }

    public List<Event> getAllUserActualEvents(Authentication authentication, int pageSize, int pageNumber) {
        return eventRepository.findUserAllActualEvents(
                userService.getUserByAuthentication(authentication),
                PageRequest.of(pageNumber, pageSize));
    }

    public List<Event> getAllUserCompletedEvents(Authentication authentication, int pageSize, int pageNumber) {
        return eventRepository.findUserAllCompletedEvents(
                userService.getUserByAuthentication(authentication),
                PageRequest.of(pageNumber, pageSize)
        );
    }

    public void followToEvent(Authentication authentication, String eventId) {
        User participant = userService.getUserByAuthentication(authentication);
        Event event = getEventById(eventId);
        performFollowingToEvent(event, participant);
    }

    private void performFollowingToEvent(Event event, User participant) {
        eventCheckerUtil.checkNonActual(event);
        eventCheckerUtil.checkStartedOrCompletedByParticipant(event, participant);
        performAddingParticipantInfoToEvent(event, participant);
    }

    private void performAddingParticipantInfoToEvent(Event event, User participant) {
        if (!event.isParticipantFollowedOnEvent(participant)) {
            eventRepository.addParticipantInfoToEvent(
                    ParticipantInfo.builder()
                            .participant(participant).build(), event);
        }
    }

    public Event getEventById(String eventObjectId) {
        return eventRepository.findEventById(new ObjectId(eventObjectId)).orElseThrow(
                () -> new EventNotFoundException(
                        eventExceptionsMessagesUtil.formEventNotFoundExceptionMessageByIdMessage(eventObjectId)));
    }

    public void unfollowFromEvent(Authentication authentication, String eventId) {
        User participant = userService.getUserByAuthentication(authentication);
        Event event = getEventById(eventId);
        performUnfollowingFromEvent(event, participant);
    }

    private void performUnfollowingFromEvent(Event event, User participant) {
        eventCheckerUtil.checkNonActual(event);
        eventCheckerUtil.checkStartedOrCompletedByParticipant(event, participant);
        performRemovingParticipantInfoFromEvent(event, participant);
    }

    private void performRemovingParticipantInfoFromEvent(Event event, User participant) {
        if (event.isParticipantFollowedOnEvent(participant)) {
            eventRepository.removeParticipantInfoFromEvent(
                    ParticipantInfo.getTaskSolutionForDeletingByParticipant(participant), event);
        }
    }

    public void startEvent(Authentication authentication, String eventId) {
        User participant = userService.getUserByAuthentication(authentication);
        Event event = getEventById(eventId);
        performFollowingToEvent(event, participant);
        performStartingEvent(event, participant);
    }

    private void performStartingEvent(Event event, User participant) {
        eventCheckerUtil.checkNonStarted(event);
        participantInfoService
                .performAddingStartingInfoToParticipantInfo(
                        participantInfoService.getParticipantInfoByEventAndParticipant(event, participant), event);
    }

    public void completeEvent(Authentication authentication, String eventId) {
        User participant = userService.getUserByAuthentication(authentication);
        Event event = getEventById(eventId);
        performCompletingEvent(event, participant);
    }

    private void performCompletingEvent(Event event, User participant) {
        eventCheckerUtil.checkNonActual(event);
        eventCheckerUtil.checkNonStartedByParticipant(event, participant);
        eventCheckerUtil.checkCompletedByParticipant(event, participant);
        ParticipantInfo participantInfo = participantInfoService.
                getParticipantInfoByEventAndParticipant(event, participant);
        participantInfoService.performAddingCompletingDateToParticipantInfo(participantInfo,
                eventDateUtil.calculateFactEndDateTime(LocalDateTime.now(), participantInfo.getMaxEndDateTime()));
    }

    public Event getEventByTaskSolutionId(String taskSolutionId) {
        return eventRepository.findEventByTaskSolutionId(new ObjectId(taskSolutionId))
                .orElseThrow(() -> new EventNotFoundException(
                        eventExceptionsMessagesUtil
                                .formEventNotFoundExceptionMessageByTaskSolutionIdMessage(taskSolutionId)));
    }

    public ParticipantInfo getParticipantInfoByConditionChecking(
            Event event, ParticipantEventConditionChecker checker, Authentication authentication) {
        User participant = userService.getUserByAuthentication(authentication);
        checker.checkEventForCondition(event, participant);
        return participantInfoService.getParticipantInfoByEventAndParticipant(event, participant);
    }

    public EventType[] getAllEventTypes() {
        return EventType.values();
    }

    public boolean isEventNameUnique(String eventName) {
        return organizationService.countOrganizationsByEventName(eventName) == 0;
    }

    public void createEvent(EventWithCreator eventWithCreator) {
        Event event = eventWithCreator.event();
        User creator = eventWithCreator.creator();
        Organization organization = getOrganizationByEventLeader(event.getEventLeader());
        eventCheckerUtil.checkValidEventLeader(organization, event.getEventLeader(), creator);
        eventCheckerUtil.checkValidEventType(creator, event.getEventType(), organization);
        organizationService.addEventToOrganization(organization, event);
    }

    private Organization getOrganizationByEventLeader(User eventLeader) {
        try {
            return organizationService.getOrganizationByLeader(eventLeader);
        } catch (OrganizationNotFoundException exception) {
            return organizationService.getOrganizationByOrganizer(eventLeader);
        }
    }

    public void changeEventLeader(Authentication authentication,
                                  EventLeaderChangingRequest eventLeaderChangingRequest) {
        Event event = getEventById(eventLeaderChangingRequest.eventId());
        Organization organization = organizationService.getOrganizationByEventContaining(event);
        User organizationLeader = userService.getUserByAuthentication(authentication);
        User newEventLeader = userService.getEventLeader(
                eventLeaderChangingRequest.newEventLeaderId(), organizationLeader);
        performEventLeaderChanging(event, organization, organizationLeader, newEventLeader);
    }

    private void checkValidEventLeaderChanging(
            Event event, Organization organization, User organizationLeader, User newEventLeader) {
        eventCheckerUtil.checkNonActual(event);
        organizationCheckerUtil.checkOrganizationLeader(organization, organizationLeader);
        eventCheckerUtil.checkLeaderAndInitiatorInSameOrganization(organization, organizationLeader, newEventLeader);
    }

    private void performEventLeaderChanging(
            Event event, Organization organization, User organizationLeader, User newEventLeader) {
        checkValidEventLeaderChanging(event, organization, organizationLeader, newEventLeader);
        eventRepository.setEventLeader(event, newEventLeader);
    }
}