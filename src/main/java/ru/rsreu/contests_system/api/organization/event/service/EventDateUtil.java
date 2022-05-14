package ru.rsreu.contests_system.api.organization.event.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventDateUtil {
    public LocalDateTime calculateMaxEndDateTime(
            LocalDateTime participantStartDateTime,
            LocalDateTime eventEndDateTime,
            long timeLimit) {
        LocalDateTime theoreticalEndDateTime = participantStartDateTime.plusSeconds(timeLimit);
        if (theoreticalEndDateTime.isAfter(eventEndDateTime) || timeLimit == 0) {
            theoreticalEndDateTime = eventEndDateTime;
        }
        return theoreticalEndDateTime;
    }

    LocalDateTime calculateFactEndDateTime(LocalDateTime now, LocalDateTime maxEndDateTime) {
        return now.isBefore(maxEndDateTime) ? now : maxEndDateTime;
    }
}
