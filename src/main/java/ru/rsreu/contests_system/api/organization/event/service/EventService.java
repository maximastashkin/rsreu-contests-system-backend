package ru.rsreu.contests_system.api.organization.event.service;

import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.exception.*;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.organization.repository.OrganizationRepository;
import ru.rsreu.contests_system.api.task.Task;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.service.UserService;
import ru.rsreu.contests_system.security.user.AuthenticationUserDetailMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public record EventService(
        OrganizationRepository organizationRepository,
        UserService userService,
        AuthenticationUserDetailMapper authenticationUserDetailMapper,
        EventExceptionsMessagesUtil eventExceptionsMessagesUtil,
        EventDateUtil eventDateUtil) {

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
        checkNonActual(event);
        checkStartedOrCompletedByParticipant(event, participant);
        performAddingParticipantInfoToEvent(event, participant);
    }

    private void performAddingParticipantInfoToEvent(Event event, User participant) {
        if (!event.isParticipantFollowedOnEvent(participant)) {
            organizationRepository.addParticipantInfoToEvent(
                    ParticipantInfo.builder()
                            .participant(participant).build(), event);
        }
    }

    private void checkStartedOrCompletedByParticipant(Event event, User participant) {
        if (event.isParticipantStartedEvent(participant) || event.isParticipantCompletedEvent(participant)) {
            throw new UserFollowingException(
                    eventExceptionsMessagesUtil.formUserFollowingExceptionMessage(participant));
        }
    }

    private void checkNonActual(Event event) {
        if (!event.isActual()) {
            throw new ActionWithNonActualEventException(
                    eventExceptionsMessagesUtil.formActionWithNonActualEventException(event));
        }
    }

    public Event getEventById(String eventObjectId) {
        return organizationRepository.findEventById(new ObjectId(eventObjectId)).orElseThrow(
                () -> new EventNotFoundException(
                        eventExceptionsMessagesUtil.formEventNotFoundExceptionMessageById(eventObjectId)));
    }

    public void unfollowFromEvent(Authentication authentication, String eventId) {
        User participant = userService.getUserByAuthentication(authentication);
        Event event = getEventById(eventId);
        performUnfollowingFromEvent(event, participant);
    }

    private void performUnfollowingFromEvent(Event event, User participant) {
        checkNonActual(event);
        checkStartedOrCompletedByParticipant(event, participant);
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
        checkNonStarted(event);
        performAddingStartingInfoToParticipantInfo(getParticipantInfoByEventAndParticipant(event, participant), event);
    }

    public ParticipantInfo getParticipantInfoByEventAndAuthentication(Event event, Authentication authentication) {
        User participant = userService.getUserByAuthentication(authentication);
        checkNonActual(event);
        checkNonStartedByParticipant(event, participant);
        checkCompletedByParticipant(event, participant);
        return getParticipantInfoByEventAndParticipant(event, participant);
    }

    private ParticipantInfo getParticipantInfoByEventAndParticipant(Event event, User participant) {
        return organizationRepository
                .findParticipantInfoByEventAndParticipant(event, participant)
                .orElseThrow(() ->
                        new ParticipantInfoNotFoundException(
                                eventExceptionsMessagesUtil.formParticipantInfoNotFoundException(participant)));
    }

    private void checkNonStarted(Event event) {
        if (!event.isStarted()) {
            throw new ActionWithNonStartedEventException(
                    eventExceptionsMessagesUtil.formActionWithNonStartedEventException(event.getId()));
        }
    }

    private void performAddingStartingInfoToParticipantInfo(ParticipantInfo participantInfo, Event event) {
        Set<TaskSolution> tasksSolutions = getTaskSolutionsFromTasks(event.getTasks());
        participantInfo.setTasksSolutions(tasksSolutions);
        participantInfo.setStartDateTime(LocalDateTime.now());
        participantInfo.setMaxEndDateTime(eventDateUtil.calculateMaxEndDateTime(
                participantInfo.getStartDateTime(), event.getEndDateTime(), event.getTimeLimit()));
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
        checkNonActual(event); //TODO Probably this checking doesn't need, because future events will solve this problem
        checkNonStartedByParticipant(event, participant);
        checkCompletedByParticipant(event, participant);
        ParticipantInfo participantInfo = getParticipantInfoByEventAndParticipant(event, participant);
        performAddingCompletingDateToParticipantInfo(participantInfo,
                eventDateUtil.calculateFactEndDateTime(LocalDateTime.now(), participantInfo.getMaxEndDateTime()));
    }

    private void checkNonStartedByParticipant(Event event, User participant) {
        if (!event.isParticipantStartedEvent(participant)) {
            throw new ActionWithNonStartedByParticipantEventException(
                    eventExceptionsMessagesUtil
                            .formActionWithNonStartedByParticipantEventExceptionMessage(event, participant));
        }
    }

    private void checkCompletedByParticipant(Event event, User participant) {
        if (event.isParticipantCompletedEvent(participant)) {
            throw new ActionWithCompletedEventException(
                    eventExceptionsMessagesUtil.formActionWithCompletedEventExceptionMessage(event, participant));
        }
    }

    private void performAddingCompletingDateToParticipantInfo(ParticipantInfo participantInfo,
                                                              LocalDateTime factEndDateTime) {
        participantInfo.setFactEndDateTime(factEndDateTime);
        organizationRepository.addFactEndDateTimeToParticipantInfo(participantInfo);
    }

    public void checkParticipantPerformingEventCondition(User participant, Event event) {
        checkNonActual(event);
        checkNonStartedByParticipant(event, participant);
        checkCompletedByParticipant(event, participant);
    }

    public Event getEventByTaskSolutionId(String taskSolutionId) {
        return organizationRepository.findEventByTaskSolutionId(new ObjectId(taskSolutionId))
                .orElseThrow(() -> new EventNotFoundException(
                        eventExceptionsMessagesUtil.formEventNotFoundExceptionMessageByTaskSolutionId(taskSolutionId)));
    }
}