package ru.rsreu.contests_system.api.organization.event.participant_info.service;

import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;

public record CompleteEventRunnableTask(
        ParticipantInfoService participantInfoService,
        ParticipantInfo participantInfo) implements Runnable {
    @Override
    public void run() {
        participantInfoService
                .performAddingCompletingDateToParticipantInfo(participantInfo, participantInfo.getMaxEndDateTime());
    }
}
