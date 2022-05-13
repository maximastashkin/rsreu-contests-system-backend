package ru.rsreu.contests_system.api.organization.event.resource.dto.event_types;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.EventType;

@Component
public class EventTypesMapper {
    public EventTypesResponse toResponse(EventType eventType) {
        return new EventTypesResponse(eventType, eventType.getStringRepresentation());
    }
}
