package ru.rsreu.contests_system.api.organization.event.participant_info.repository;

import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.user.User;

import java.util.List;
import java.util.Optional;

public interface ParticipantInfoRepository {
    Optional<ParticipantInfo> findParticipantInfoByEventAndParticipant(Event event, User participant);

    void addStartingInfoToParticipantInfo(ParticipantInfo participantInfo);

    void addFactEndDateTimeToParticipantInfo(ParticipantInfo participantInfo);

    List<ParticipantInfo> findAllNotCompletedParticipantsInfos();
}
