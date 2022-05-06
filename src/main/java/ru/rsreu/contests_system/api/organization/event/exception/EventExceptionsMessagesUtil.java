package ru.rsreu.contests_system.api.organization.event.exception;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;

@Component
public class EventExceptionsMessagesUtil {
    public String formActionWithNonActualEventException(Event event) {
        return String.format(
                "Unable to perform participant action with event with id:%s, because it's non-actual",
                event.getId());
    }

    public String formUserFollowingExceptionMessage(User participant) {
        return String.format("User with email:%s already start or completed this event", participant.getEmail());
    }

    public String formParticipantInfoNotFoundException(User participant) {
        return String.format("Critical error. Server didn't create ParticipantInfo for user with email:%s",
                participant.getEmail());
    }

    public String formActionWithNonStartedEventException(Event event) {
        return String.format("Event with id:%s non started yet. Following have done", event.getId().toString());
    }

    public String formActionWithNonStartedByParticipantEventExceptionMessage(Event event, User participant) {
        return String.format("Participant with email:%s didn't start the event with id:%s",
                participant.getEmail(), event.getId());
    }

    public String formActionWithCompletedEventExceptionMessage(Event event, User participant) {
        return String.format("Participant with email:%s have already completed the event with id:%s",
                participant.getEmail(), event.getId());
    }

    public String formEventNotFoundExceptionMessageById(String eventObjectId) {
        return String.format("Event with id:%s didn't find", eventObjectId);
    }

    public String formEventNotFoundExceptionMessageByTaskSolutionId(String taskSolutionId) {
        return String.format("Event with TaskSolution with id:%s didn't found. In fact that TaskSolution doesn't exist",
                taskSolutionId);
    }

    public String formActionWithNonFinishedEventException(Event event) {
        return String.format("Event with id:%s haven't finished yet", event.getId().toString());
    }

    public String formActionWithNonCompletedByParticipantEventException(Event event, User participant) {
        return String.format("User with email:%s hasn't completed event with id:%s",
                participant.getEmail(), event.getId().toString());
    }
}
