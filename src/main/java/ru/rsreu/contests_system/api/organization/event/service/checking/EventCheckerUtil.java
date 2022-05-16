package ru.rsreu.contests_system.api.organization.event.service.checking;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.EventType;
import ru.rsreu.contests_system.api.organization.event.exception.*;
import ru.rsreu.contests_system.api.organization.service.OrganizationCheckerUtil;
import ru.rsreu.contests_system.api.user.User;

@Component
public record EventCheckerUtil(EventExceptionsMessagesUtil eventExceptionsMessagesUtil,
                               OrganizationCheckerUtil organizationCheckerUtil) {
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
                    .formActionWithNonFinishedEventExceptionMessage(event));
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
                            .formActionWithNonCompletedByParticipantEventExceptionMessage(event, participant));
        }
    }

    public void checkNonStarted(Event event) {
        if (!event.isStarted()) {
            throw new ActionWithNonStartedEventException(
                    eventExceptionsMessagesUtil.formActionWithNonStartedEventExceptionMessage(event));
        }
    }

    public void checkLeaderAndInitiatorInSameOrganization(Organization organization, User eventLeader, User initiator) {
        if (!(organization.isUserInOrganization(initiator) && organization.isUserInOrganization(eventLeader))) {
            throw new EventLeaderAndCreatorNotInSameOrganizationException(
                    eventExceptionsMessagesUtil.formEventLeaderAndCreatorNotInSameOrganizationExceptionMessage(
                            eventLeader, initiator
                    )
            );
        }
    }

    public void checkValidEventLeader(Organization organization, User eventLeader, User eventCreator) {
        checkLeaderAndInitiatorInSameOrganization(organization, eventLeader, eventCreator);
        checkOrganizerNotMadeOrganizationLeaderAsEventLeader(organization, eventLeader, eventCreator);
    }

    private void checkOrganizerNotMadeOrganizationLeaderAsEventLeader(
            Organization organization, User eventLeader, User eventCreator) {
        if (organization.isLeader(eventLeader) && !eventLeader.equals(eventCreator)) {
            throw new AppointmentOrganizationLeaderAsEventLeaderException(
                    eventExceptionsMessagesUtil.formAppointmentOrganizationLeaderEventLeaderExceptionMessage(
                            eventLeader, eventCreator)
            );
        }
    }

    public void checkValidEventType(User creator, EventType eventType, Organization organization) {
        if (!organization.isLeader(creator)) {
            checkValidEventTypeForOrganizer(eventType);
        }
    }

    private void checkValidEventTypeForOrganizer(EventType eventType) {
        if (!EventType.getAvailableForOrganizersTypes().contains(eventType)) {
            throw new NotAvailableForOrganizerEventType(
                    eventExceptionsMessagesUtil().formNotAvailableForOrganizerEventType(eventType));
        }
    }
}
