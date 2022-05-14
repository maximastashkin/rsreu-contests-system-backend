package ru.rsreu.contests_system.api.organization.event.resource.dto.event_creating;

import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;

public record EventWithCreator(Event event, User creator) {
}
