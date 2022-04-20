package ru.rsreu.contests_system.config.mongo;

import lombok.AllArgsConstructor;

import java.time.ZoneOffset;

@AllArgsConstructor
public abstract class DateConverter {
    protected final ZoneOffset zoneOffset;
}
