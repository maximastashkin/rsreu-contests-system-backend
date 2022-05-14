package ru.rsreu.contests_system.api.organization.event;

import lombok.Getter;

import java.util.EnumSet;

@Getter
public enum EventType {
    OLYMPIAD("Олимпиада"),
    CONTROL("Контроль");

    private final String stringRepresentation;

    EventType(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    public static EnumSet<EventType> getAvailableForOrganizersTypes() {
        return EnumSet.of(CONTROL);
    }
}
