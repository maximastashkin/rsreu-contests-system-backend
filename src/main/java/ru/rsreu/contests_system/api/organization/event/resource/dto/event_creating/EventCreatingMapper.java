package ru.rsreu.contests_system.api.organization.event.resource.dto.event_creating;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.exception.EventExceptionsMessagesUtil;
import ru.rsreu.contests_system.api.organization.event.exception.EventWrongDatesOrderException;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.service.UserService;

@Component
public record EventCreatingMapper(
        UserService userService,
        EventExceptionsMessagesUtil eventExceptionsMessagesUtil) {
    public EventWithCreator toEventWithCreator(EventCreatingRequest eventCreatingRequest,
                                               Authentication authentication) {
        User creator = userService.getUserByAuthentication(authentication);
        Event event = toEvent(eventCreatingRequest);
        event.setEventLeader(getEventLeader(eventCreatingRequest, creator));
        return new EventWithCreator(event, creator);
    }

    private Event toEvent(EventCreatingRequest eventCreatingRequest) {
        checkWrongDatesOrder(eventCreatingRequest);
        return Event.builder()
                .name(eventCreatingRequest.name().trim())
                .description(eventCreatingRequest.getDescription())
                .eventType(eventCreatingRequest.eventType())
                .startDateTime(eventCreatingRequest.startDateTime())
                .endDateTime(eventCreatingRequest.endDateTime())
                .build();
    }

    private User getEventLeader(EventCreatingRequest eventCreatingRequest, User creator) {
        if (eventCreatingRequest.eventLeaderId() != null) {
            return userService.getUserById(eventCreatingRequest.eventLeaderId());
        }
        return creator;
    }

    private void checkWrongDatesOrder(EventCreatingRequest eventCreatingRequest) {
        if (eventCreatingRequest.startDateTime().isAfter(eventCreatingRequest.endDateTime())) {
            throw new EventWrongDatesOrderException(eventExceptionsMessagesUtil
                    .formEventWrongDatesOrderExceptionMessage());
        }
    }
}
