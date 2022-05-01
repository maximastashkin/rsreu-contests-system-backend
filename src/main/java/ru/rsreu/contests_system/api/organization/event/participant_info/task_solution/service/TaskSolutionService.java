package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service;

import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ExecutionStatus;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ProgrammingLanguage;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TestInfo;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.exception.TaskSolutionExceptionMessageUtil;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.exception.TaskSolutionForParticipantNotFoundException;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.task_checking.TaskCheckingRequest;
import ru.rsreu.contests_system.api.organization.event.service.EventService;
import ru.rsreu.contests_system.api.organization.repository.OrganizationRepository;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.service.UserService;

@Service
public record TaskSolutionService(
        UserService userService,
        EventService eventService,
        OrganizationRepository organizationRepository,
        TaskSolutionExceptionMessageUtil taskSolutionExceptionMessageUtil) {

    public TaskSolution getTaskSolutionByAuthenticationAndId(Authentication authentication, String id) {
        User participant = userService.getUserByAuthentication(authentication);
        Event event = eventService.getEventByTaskSolutionId(id);
        eventService.checkParticipantPerformingEventCondition(participant, event);
        return getTaskSolutionByIdAndParticipant(id, participant);
    }

    private TaskSolution getTaskSolutionByIdAndParticipant(String id, User participant) {
        return organizationRepository.findParticipantTaskSolutionById(participant, new ObjectId(id)).orElseThrow(
                () -> new TaskSolutionForParticipantNotFoundException(
                        taskSolutionExceptionMessageUtil.
                                formTaskSolutionForParticipantNotFoundException(participant, id)));
    }

    public void checkTask(Authentication authentication, String id, TaskCheckingRequest request) {
        TaskSolution taskSolution = getTaskSolutionByAuthenticationAndId(authentication, id);
        setTaskSolutionCheckingInfoFromRequest(request, taskSolution);
        organizationRepository.setTaskSolutionCheckingInfo(taskSolution);
    }

    private void setTaskSolutionCheckingInfoFromRequest(TaskCheckingRequest request, TaskSolution taskSolution) {
        taskSolution.setExecutionStatus(ExecutionStatus.ON_CHECKING);
        taskSolution.setSolution(request.solution());
        taskSolution.setProgrammingLanguage(ProgrammingLanguage.valueOf(request.language().toUpperCase()));
        taskSolution.getTestsInfos().forEach(this::setTestInfoForChecking);
    }

    private void setTestInfoForChecking(TestInfo testInfo) {
        testInfo.setExecutionStatus(ExecutionStatus.ON_CHECKING);
        testInfo.setMemoryKb(0);
        testInfo.setTimeMs(0);
        testInfo.setTestPassed(false);
        testInfo.setExecutionOutput("");
    }
}
