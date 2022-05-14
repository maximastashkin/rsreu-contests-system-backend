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
import ru.rsreu.contests_system.api.organization.event.exception.ParticipantInfoNotFoundException;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.organization.event.resource.dto.event_creating.EventWithCreator;
import ru.rsreu.contests_system.api.organization.event.service.checking.EventCheckerUtil;
import ru.rsreu.contests_system.api.organization.event.service.checking.ParticipantEventConditionChecker;
import ru.rsreu.contests_system.api.organization.exception.OrganizationNotFoundException;
import ru.rsreu.contests_system.api.organization.repository.OrganizationRepository;
import ru.rsreu.contests_system.api.organization.service.OrganizationService;
import ru.rsreu.contests_system.api.task.Task;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.service.UserService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public record EventService(
        OrganizationRepository organizationRepository,
        OrganizationService organizationService,
        UserService userService,
        EventExceptionsMessagesUtil eventExceptionsMessagesUtil,
        EventDateUtil eventDateUtil,
        EventCompletingTasksHolder tasksHolder,
        EventCheckerUtil eventCheckerUtil) {
    @PostConstruct
    public void initAllNotCompletedParticipantsInfosTasks() {
        organizationRepository.findAllNotCompletedParticipantsInfos().forEach(
                participantInfo ->
                        tasksHolder.addTask(new CompleteEventRunnableTask(this, participantInfo)));
    }

    public List<Event> getAllActualEvents(int pageSize, int pageNumber) {
        return organizationRepository.findAllActualEvents(PageRequest.of(pageNumber, pageSize));
    }

    public List<Event> getAllUserActualEvents(Authentication authentication, int pageSize, int pageNumber) {
        return organizationRepository.findUserAllActualEvents(
                userService.getUserByAuthentication(authentication),
                PageRequest.of(pageNumber, pageSize));
    }

    public List<Event> getAllUserCompletedEvents(Authentication authentication, int pageSize, int pageNumber) {
        return organizationRepository.findUserAllCompletedEvents(
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
            organizationRepository.addParticipantInfoToEvent(
                    ParticipantInfo.builder()
                            .participant(participant).build(), event);
        }
    }

    public Event getEventById(String eventObjectId) {
        return organizationRepository.findEventById(new ObjectId(eventObjectId)).orElseThrow(
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
            organizationRepository.removeParticipantInfoFromEvent(
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
        performAddingStartingInfoToParticipantInfo(getParticipantInfoByEventAndParticipant(event, participant), event);
    }

    private ParticipantInfo getParticipantInfoByEventAndParticipant(Event event, User participant) {
        return organizationRepository
                .findParticipantInfoByEventAndParticipant(event, participant)
                .orElseThrow(() ->
                        new ParticipantInfoNotFoundException(
                                eventExceptionsMessagesUtil.formParticipantInfoNotFoundExceptionMessage(participant)));
    }

    private void performAddingStartingInfoToParticipantInfo(ParticipantInfo participantInfo, Event event) {
        Set<TaskSolution> tasksSolutions = getTaskSolutionsFromTasks(event.getTasks());
        participantInfo.setTasksSolutions(tasksSolutions);
        participantInfo.setStartDateTime(LocalDateTime.now());
        participantInfo.setMaxEndDateTime(eventDateUtil.calculateMaxEndDateTime(
                participantInfo.getStartDateTime(), event.getEndDateTime(), event.getTimeLimit()));
        tasksHolder.addTask(new CompleteEventRunnableTask(this, participantInfo));
        organizationRepository.addStartingInfoToParticipantInfo(participantInfo);
    }

    private Set<TaskSolution> getTaskSolutionsFromTasks(Set<Task> tasks) {
        return tasks.stream().map(TaskSolution::getTaskSolutionByTask).collect(Collectors.toSet());
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
        ParticipantInfo participantInfo = getParticipantInfoByEventAndParticipant(event, participant);
        performAddingCompletingDateToParticipantInfo(participantInfo,
                eventDateUtil.calculateFactEndDateTime(LocalDateTime.now(), participantInfo.getMaxEndDateTime()));
    }

    void performAddingCompletingDateToParticipantInfo(ParticipantInfo participantInfo,
                                                      LocalDateTime factEndDateTime) {
        tasksHolder.cancelTaskByParticipantInfo(participantInfo);
        participantInfo.setFactEndDateTime(factEndDateTime);
        organizationRepository.addFactEndDateTimeToParticipantInfo(participantInfo);
    }

    public Event getEventByTaskSolutionId(String taskSolutionId) {
        return organizationRepository.findEventByTaskSolutionId(new ObjectId(taskSolutionId))
                .orElseThrow(() -> new EventNotFoundException(
                        eventExceptionsMessagesUtil
                                .formEventNotFoundExceptionMessageByTaskSolutionIdMessage(taskSolutionId)));
    }

    public ParticipantInfo getParticipantInfoByConditionChecking(
            Event event, ParticipantEventConditionChecker checker, Authentication authentication) {
        User participant = userService.getUserByAuthentication(authentication);
        checker.checkEventForCondition(event, participant);
        return getParticipantInfoByEventAndParticipant(event, participant);
    }

    public EventType[] getAllEventTypes() {
        return EventType.values();
    }

    public boolean isEventNameUnique(String eventName) {
        return organizationRepository.countAllByEventName(eventName) == 0;
    }

    public void createEvent(EventWithCreator eventWithCreator) {
        Event event = eventWithCreator.event();
        User creator = eventWithCreator.creator();
        Organization organization = getOrganizationByEventLeader(event.getEventLeader());
        eventCheckerUtil.checkValidEventLeader(organization, event.getEventLeader(), creator);
        eventCheckerUtil.checkValidEventType(creator, event.getEventType(), organization);
        organizationRepository.addEventToOrganization(organization, event);
    }

    private Organization getOrganizationByEventLeader(User eventLeader) {
        try {
            return organizationService.getOrganizationByLeader(eventLeader);
        } catch (OrganizationNotFoundException exception) {
            return organizationService.getOrganizationByOrganizer(eventLeader);
        }
    }
}