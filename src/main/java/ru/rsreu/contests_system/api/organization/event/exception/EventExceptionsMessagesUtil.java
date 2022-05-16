package ru.rsreu.contests_system.api.organization.event.exception;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.EventType;
import ru.rsreu.contests_system.api.user.User;

@Component
public class EventExceptionsMessagesUtil {
    public String formActionWithNonActualEventException(Event event) {
        return String.format(
                "Unable to perform action with event with id:%s, because it's non-actual",
                event.getId());
    }

    public String formUserFollowingExceptionMessage(User participant) {
        return String.format("User with email:%s already start or completed this event", participant.getEmail());
    }

    public String formActionWithNonStartedEventExceptionMessage(Event event) {
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

    public String formEventNotFoundExceptionMessageByIdMessage(String eventObjectId) {
        return String.format("Event with id:%s didn't find", eventObjectId);
    }

    public String formEventNotFoundExceptionMessageByTaskSolutionIdMessage(String taskSolutionId) {
        return String.format("Event with TaskSolution with id:%s didn't found. In fact that TaskSolution doesn't exist",
                taskSolutionId);
    }

    public String formActionWithNonFinishedEventExceptionMessage(Event event) {
        return String.format("Event with id:%s haven't finished yet", event.getId().toString());
    }

    public String formActionWithNonCompletedByParticipantEventExceptionMessage(Event event, User participant) {
        return String.format("User with email:%s hasn't completed event with id:%s",
                participant.getEmail(), event.getId().toString());
    }

    public String formEventWrongDatesOrderExceptionMessage() {
        return "Wrong dates order!";
    }

    public String formNotAvailableForOrganizerEventType(EventType eventType) {
        return eventType.toString() + " not available for creating by organizers";
    }

    public String formEventLeaderAndCreatorNotInSameOrganizationExceptionMessage(User leader, User creator) {
        return String.format("Event leader with id:%s and event creator with id:%s isn't in same organization",
                leader.getId().toString(), creator.getId().toString());
    }

    public String formAppointmentOrganizationLeaderEventLeaderExceptionMessage(User leader, User creator) {
        return String.format("Attempt to appointment organization leader (id:%s) as event leader by organizer (id:%s)",
                leader.getId().toString(), creator.getId().toString());
    }

    public String formNotUniqueEventNameException(String eventName) {
        return String.format("Event with name '%s' already exists", eventName);
    }
}
