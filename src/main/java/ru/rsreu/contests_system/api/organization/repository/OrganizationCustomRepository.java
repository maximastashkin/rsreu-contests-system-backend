package ru.rsreu.contests_system.api.organization.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.user.User;

import java.util.List;
import java.util.Optional;

public interface OrganizationCustomRepository {
    List<Event> findAllActualEvents(Pageable pageable);

    List<Event> findUserAllActualEvents(User user, Pageable pageable);

    List<Event> findUserAllCompletedEvents(User user, Pageable pageable);

    void addParticipantInfoToEvent(ParticipantInfo participantInfo, Event event);

    Optional<Event> findEventById(ObjectId eventId);

    void removeParticipantInfoFromEvent(ParticipantInfo participantInfo, Event event);

    void addStartingInfoToParticipantInfo(ParticipantInfo participantInfo);

    Optional<ParticipantInfo> findParticipantInfoByEventAndParticipant(Event event, User participant);

    void addFactEndDateTimeToParticipantInfo(ParticipantInfo participantInfo);

    Optional<Event> findEventByTaskSolutionId(ObjectId taskSolutionId);

    Optional<TaskSolution> findParticipantTaskSolutionById(User participant, ObjectId taskSolutionId);

    void setTaskSolutionCheckingInfo(TaskSolution taskSolution);

    void setTaskSolutionCheckingResultInfo(TaskSolution taskSolution);
}
