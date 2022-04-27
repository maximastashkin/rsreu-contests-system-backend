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
        AuthenticationUserDetailMapper authenticationUserDetailMapper) {

    public List<Event> getAllActualEvents(int pageSize, int pageNumber) {
        return organizationRepository.findAllActualEvents(PageRequest.of(pageNumber, pageSize));
    }

    public List<Event> getAllUserActualEvents(Authentication authentication, int pageSize, int pageNumber) {
        return organizationRepository.findUserAllActualEvents(
                getUserByAuthentication(authentication), PageRequest.of(pageNumber, pageSize));
    }

    private User getUserByAuthentication(Authentication authentication) {
        return userService.getUserByEmail(
                authenticationUserDetailMapper.toUserDetails(authentication).getUsername());
    }

    public List<Event> getAllUserCompletedEvents(Authentication authentication, int pageSize, int pageNumber) {
        return organizationRepository.findUserAllCompletedEvents(
                getUserByAuthentication(authentication), PageRequest.of(pageNumber, pageSize)
        );
    }

    public void followToEvent(Authentication authentication, String eventId) {
        User participant = getUserByAuthentication(authentication);
        Event event = getEventById(new ObjectId(eventId));
        performFollowingToEvent(participant, event);
    }

    private void performFollowingToEvent(User participant, Event event) {
        checkActual(event);
        checkStartedOrCompletedByParticipant(participant, event);
        performAddingParticipantInfoToEvent(participant, event);
    }

    private void performAddingParticipantInfoToEvent(User participant, Event event) {
        if (!event.isParticipantFollowedOnEvent(participant)) {
            organizationRepository.addParticipantInfoToEvent(
                    ParticipantInfo.builder()
                            .participant(participant).build(), event);
        }
    }

    private void checkStartedOrCompletedByParticipant(User participant, Event event) {
        if (event.isParticipantStartedEvent(participant) || event.isParticipantCompletedEvent(participant)) {
            throw new UserFollowingException(formUserFollowingExceptionMessage(participant));
        }
    }

    private void checkActual(Event event) {
        if (!event.isActual()) {
            throw new ActionWithNonActualEventException(formActionWithNonActualEventException(event));
        }
    }

    private String formActionWithNonActualEventException(Event event) {
        return String.format(
                "Unable to perform participant action with event with id:%s, because it's non-actual",
                event.getId());
    }

    private String formUserFollowingExceptionMessage(User participant) {
        return String.format("User with email:%s already start or completed this event", participant.getEmail());
    }

    private Event getEventById(ObjectId eventObjectId) {
        return organizationRepository.findEventById(eventObjectId).orElseThrow(
                () -> new EventNotFoundException(String.format("Event with id:%s didn't found",
                        eventObjectId.toString())));
    }

    public void unfollowFromEvent(Authentication authentication, String eventId) {
        User participant = getUserByAuthentication(authentication);
        Event event = getEventById(new ObjectId(eventId));
        performUnfollowingFromEvent(participant, event);
    }

    private void performUnfollowingFromEvent(User participant, Event event) {
        checkActual(event);
        checkStartedOrCompletedByParticipant(participant, event);
        performRemovingParticipantInfoFromEvent(participant, event);
    }

    private void performRemovingParticipantInfoFromEvent(User participant, Event event) {
        if (event.isParticipantFollowedOnEvent(participant)) {
            organizationRepository.removeParticipantInfoFromEvent(
                    ParticipantInfo.getTaskSolutionForDeletingByParticipant(participant), event);
        }
    }

    public void startEvent(Authentication authentication, String eventId) {
        User participant = getUserByAuthentication(authentication);
        Event event = getEventById(new ObjectId(eventId));
        performFollowingToEvent(participant, event);
        performStartingEvent(participant, event);
    }

    private void performStartingEvent(User participant, Event event) {
        checkStarted(event);
        ParticipantInfo participantInfo = organizationRepository
                .findParticipantInfoByEventAndParticipant(event, participant)
                .orElseThrow(() ->
                        new ParticipantInfoNotFoundException(formParticipantInfoNotFoundException(participant)));
        performAddingStartingInfoToParticipantInfo(participantInfo, event);
    }

    private String formParticipantInfoNotFoundException(User participant) {
        return String.format("Critical error. Server didn't create ParticipantInfo for user with email:%s",
                participant.getEmail());
    }

    private void checkStarted(Event event) {
        if (!event.isStarted()) {
            throw new ActionWithNonStartedEventException(formActionWithNonStartedEventException(event.getId()));
        }
    }

    private String formActionWithNonStartedEventException(ObjectId eventObjectId){
        return String.format("Event with id:%s non started yet. Following have done", eventObjectId.toString());
    }

    private void performAddingStartingInfoToParticipantInfo(ParticipantInfo participantInfo, Event event) {
        Set<TaskSolution> tasksSolutions = getTaskSolutionsFromTasks(event.getTasks());
        participantInfo.setTasksSolutions(tasksSolutions);
        participantInfo.setStartDateTime(LocalDateTime.now());
        participantInfo.setMaxEndDateTime(calculateMaxEndDateTime(
                participantInfo.getStartDateTime(), event.getEndDateTime(), event.getTimeLimit()));
        organizationRepository.addStartingInfoToParticipantInfo(participantInfo);
    }

    private Set<TaskSolution> getTaskSolutionsFromTasks(Set<Task> tasks) {
        return tasks.stream().map(TaskSolution::getTaskSolutionByTask).collect(Collectors.toSet());
    }

    private LocalDateTime calculateMaxEndDateTime(
            LocalDateTime participantStartDateTime,
            LocalDateTime eventEndDateTime,
            long timeLimit) {
        LocalDateTime theoreticalEndDateTime = participantStartDateTime.plusSeconds(timeLimit);
        if (theoreticalEndDateTime.isAfter(eventEndDateTime)) {
            theoreticalEndDateTime = eventEndDateTime;
        }
        return theoreticalEndDateTime;
    }
}