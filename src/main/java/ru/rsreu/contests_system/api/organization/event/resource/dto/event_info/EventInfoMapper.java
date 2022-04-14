package ru.rsreu.contests_system.api.organization.event.resource.dto.event_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;

@Component
public class EventInfoMapper {
    public EventInfoResponse toResponse(Event event) {
        return new EventInfoResponse(
                event.getName(),
                event.getDescription(),
                event.getStartDateTime(),
                event.getEndDateTime()
        );
    }
}
