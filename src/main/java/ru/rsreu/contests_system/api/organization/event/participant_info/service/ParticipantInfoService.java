package ru.rsreu.contests_system.api.organization.event.participant_info.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.organization.event.participant_info.exception.ParticipantInfoExceptionsMessagesUtil;
import ru.rsreu.contests_system.api.organization.event.participant_info.exception.ParticipantInfoNotFoundException;
import ru.rsreu.contests_system.api.organization.event.participant_info.repository.ParticipantInfoRepository;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.organization.event.service.EventDateUtil;
import ru.rsreu.contests_system.api.task.Task;
import ru.rsreu.contests_system.api.user.User;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("ClassCanBeRecord")
@Service
@AllArgsConstructor
public class ParticipantInfoService {
    private final ParticipantInfoRepository participantInfoRepository;
    private final ParticipantInfoExceptionsMessagesUtil participantInfoExceptionsMessagesUtil;
    private final EventCompletingTasksHolder tasksHolder;
    private final EventDateUtil eventDateUtil;

    @PostConstruct
    private void initAllNotCompletedParticipantsInfosTasks() {
        participantInfoRepository.findAllNotCompletedParticipantsInfos().forEach(
                participantInfo ->
                        tasksHolder.addTask(new CompleteEventRunnableTask(this, participantInfo)));
    }

    public ParticipantInfo getParticipantInfoByEventAndParticipant(Event event, User participant) {
        return participantInfoRepository
                .findParticipantInfoByEventAndParticipant(event, participant)
                .orElseThrow(() ->
                        new ParticipantInfoNotFoundException(participantInfoExceptionsMessagesUtil
                                .formParticipantInfoNotFoundExceptionMessage(participant)));
    }

    public void performAddingStartingInfoToParticipantInfo(ParticipantInfo participantInfo, Event event) {
        Set<TaskSolution> tasksSolutions = getTaskSolutionsFromTasks(event.getTasks());
        participantInfo.setTasksSolutions(tasksSolutions);
        participantInfo.setStartDateTime(LocalDateTime.now());
        participantInfo.setMaxEndDateTime(eventDateUtil.calculateMaxEndDateTime(
                participantInfo.getStartDateTime(), event.getEndDateTime(), event.getTimeLimit()));
        tasksHolder.addTask(new CompleteEventRunnableTask(this, participantInfo));
        participantInfoRepository.addStartingInfoToParticipantInfo(participantInfo);
    }

    private Set<TaskSolution> getTaskSolutionsFromTasks(Set<Task> tasks) {
        return tasks.stream().map(TaskSolution::getTaskSolutionByTask).collect(Collectors.toSet());
    }

    public void performAddingCompletingDateToParticipantInfo(ParticipantInfo participantInfo,
                                                             LocalDateTime factEndDateTime) {
        tasksHolder.cancelTaskByParticipantInfo(participantInfo);
        participantInfo.setFactEndDateTime(factEndDateTime);
        participantInfoRepository.addFactEndDateTimeToParticipantInfo(participantInfo);
    }
}
