package ru.rsreu.contests_system.api.organization.event.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.user.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Optional<Event> findEventById(ObjectId eventId);

    List<Event> findAllActualEvents(Pageable pageable);

    List<Event> findUserAllActualEvents(User user, Pageable pageable);

    List<Event> findUserAllCompletedEvents(User user, Pageable pageable);

    void addParticipantInfoToEvent(ParticipantInfo participantInfo, Event event);

    void removeParticipantInfoFromEvent(ParticipantInfo participantInfo, Event event);

    Optional<Event> findEventByTaskSolutionId(ObjectId taskSolutionId);

    void setEventLeader(Event event, User leader);

    Optional<Event> findEventByName(String name);
}
