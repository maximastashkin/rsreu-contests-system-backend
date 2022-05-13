package ru.rsreu.contests_system.api.organization.event;

import lombok.Getter;

@Getter
public enum EventType {
    OLYMPIAD("Олимпиада"),
    CONTROL("Контроль");

    private final String stringRepresentation;

    EventType(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
}
