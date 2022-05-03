package ru.rsreu.contests_system.api.organization.event.service;

import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;

public record CompleteEventRunnableTask(
        EventService eventService,
        ParticipantInfo participantInfo) implements Runnable {
    @Override
    public void run() {
        eventService
                .performAddingCompletingDateToParticipantInfo(participantInfo, participantInfo.getMaxEndDateTime());
    }
}
