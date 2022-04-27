package ru.rsreu.contests_system.api.organization.event.service;

import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.exception.ActionWithNonActualEventException;
import ru.rsreu.contests_system.api.organization.event.exception.EventNotFoundException;
import ru.rsreu.contests_system.api.organization.event.exception.UserFollowingException;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.organization.repository.OrganizationRepository;
import ru.rsreu.contests_system.api.task.Task;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.service.UserService;
import ru.rsreu.contests_system.security.user.AuthenticationUserDetailMapper;

import javax.servlet.http.Part;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        checkStartedOrCompleted(participant, event);
        performAddingParticipantInfoToEvent(participant, event);
    }

    private void performAddingParticipantInfoToEvent(User participant, Event event) {
        if (!event.isParticipantFollowedOnEvent(participant)) {
            organizationRepository.addParticipantInfoToEvent(
                    ParticipantInfo.builder()
                            .participant(participant).build(), event);
        }
    }

    private void checkStartedOrCompleted(User participant, Event event) {
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
        checkStartedOrCompleted(participant, event);
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
        performStartingEvent(participant, event);
    }

    private void performStartingEvent(User participant, Event event) {
        performFollowingToEvent(participant, event);
        Set<Task> tasks = event.getTasks();
        List<TaskSolution> tasksSolutions =
                tasks.stream().map(task -> TaskSolution.builder().task(task).build()).toList();
        ParticipantInfo participantInfo =
                organizationRepository.findParticipantInfoByEventAndParticipant(event, participant).orElseThrow(IllegalArgumentException::new);
        participantInfo.setTasksSolutions(new HashSet<>(tasksSolutions));
        participantInfo.setStartDateTime(LocalDateTime.now());
        organizationRepository.addStartingInfoToParticipantInfo(participantInfo);
    }
}
