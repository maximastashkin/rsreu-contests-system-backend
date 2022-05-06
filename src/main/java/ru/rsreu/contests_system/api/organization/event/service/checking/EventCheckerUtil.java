package ru.rsreu.contests_system.api.organization.event.service.checking;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.exception.*;
import ru.rsreu.contests_system.api.user.User;

@Component
public record EventCheckerUtil(EventExceptionsMessagesUtil eventExceptionsMessagesUtil) {
    public void checkStartedOrCompletedByParticipant(Event event, User participant) {
        if (event.isParticipantStartedEvent(participant) || event.isParticipantCompletedEvent(participant)) {
            throw new UserFollowingException(
                    eventExceptionsMessagesUtil.formUserFollowingExceptionMessage(participant));
        }
    }

    public void checkNonActual(Event event) {
        if (!event.isActual()) {
            throw new ActionWithNonActualEventException(
                    eventExceptionsMessagesUtil.formActionWithNonActualEventException(event));
        }
    }

    public void checkFinished(Event event) {
        if (event.isActual()) {
            throw new ActionWithNonFinishedEventException(eventExceptionsMessagesUtil()
                    .formActionWithNonFinishedEventException(event));
        }
    }

    public void checkNonStartedByParticipant(Event event, User participant) {
        if (!event.isParticipantStartedEvent(participant)) {
            throw new ActionWithNonStartedByParticipantEventException(
                    eventExceptionsMessagesUtil
                            .formActionWithNonStartedByParticipantEventExceptionMessage(event, participant));
        }
    }

    public void checkCompletedByParticipant(Event event, User participant) {
        if (event.isParticipantCompletedEvent(participant)) {
            throw new ActionWithCompletedEventException(
                    eventExceptionsMessagesUtil.formActionWithCompletedEventExceptionMessage(event, participant));
        }
    }

    public void checkNonCompletedByParticipant(Event event, User participant) {
        if (!event.isParticipantCompletedEvent(participant)) {
            throw new ActionWithNonCompletedByParticipantEventException(
                    eventExceptionsMessagesUtil()
                            .formActionWithNonCompletedByParticipantEventException(event, participant));
        }
    }
}
