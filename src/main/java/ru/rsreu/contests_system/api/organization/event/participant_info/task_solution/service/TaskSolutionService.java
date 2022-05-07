package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ExecutionStatus;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ProgrammingLanguage;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TestInfo;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.exception.RustCodeExecutorServiceNonAvailableException;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.exception.TaskSolutionExceptionMessageUtil;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.exception.TaskSolutionForParticipantNotFoundException;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.task_checking.TaskCheckingRequest;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.CodeExecutorServiceProvider;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.response.ExecutionResponse;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.response.ExecutionTest;
import ru.rsreu.contests_system.api.organization.event.service.EventService;
import ru.rsreu.contests_system.api.organization.event.service.checking.ParticipantEventConditionChecker;
import ru.rsreu.contests_system.api.organization.event.service.checking.ParticipantPerformingEventConditionChecker;
import ru.rsreu.contests_system.api.organization.repository.OrganizationRepository;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.service.UserService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class TaskSolutionService {
    private final UserService userService;
    private final EventService eventService;
    private final OrganizationRepository organizationRepository;
    private final TaskSolutionExceptionMessageUtil taskSolutionExceptionMessageUtil;
    private final CodeExecutorServiceProvider codeExecutorServiceProvider;
    private final ParticipantPerformingEventConditionChecker performingEventConditionChecker;

    @Value("${code_executor_service.seconds_timeout}")
    private Integer codeExecutorServiceTimeout;

    private TaskSolution getTaskSolutionByIdAndParticipant(String id, User participant) {
        return organizationRepository.findParticipantTaskSolutionById(participant, new ObjectId(id)).orElseThrow(
                () -> new TaskSolutionForParticipantNotFoundException(
                        taskSolutionExceptionMessageUtil.
                                formTaskSolutionForParticipantNotFoundException(participant, id)));
    }

    public TaskSolution prepareTaskSolutionForChecking(Authentication authentication, String id,
                                                       TaskCheckingRequest request) {
        TaskSolution taskSolution = getTaskSolutionByConditionChecking(
                authentication, performingEventConditionChecker, id);
        setTaskSolutionCheckingInfoFromRequest(request, taskSolution);
        organizationRepository.setTaskSolutionCheckingInfo(taskSolution);
        return taskSolution;
    }

    private void setTaskSolutionCheckingInfoFromRequest(TaskCheckingRequest request, TaskSolution taskSolution) {
        taskSolution.setExecutionStatus(ExecutionStatus.ON_CHECKING);
        taskSolution.setSolution(request.solution());
        taskSolution.setProgrammingLanguage(ProgrammingLanguage.valueOf(request.language().toUpperCase()));
        taskSolution.getTestsInfos().forEach(TestInfo::setOnCheckingStatus);
    }

    @Async
    public void asyncPerformCheckingTaskSolution(TaskSolution taskSolution) {
        try {
            CompletableFuture<ExecutionResponse> whenResponse = codeExecutorServiceProvider
                    .asyncExecuteTaskSolution(taskSolution);
            ExecutionResponse executionResponse = whenResponse.get(codeExecutorServiceTimeout, TimeUnit.SECONDS);
            if (whenResponse.isDone()) {
                setTaskSolutionInfoAfterChecking(taskSolution, executionResponse);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            setTaskSolutionInfoAfterChecking(taskSolution,
                    ExecutionResponse.getCheckingFailExecutionResponse());
        } finally {
            organizationRepository.setTaskSolutionCheckingResultInfo(taskSolution);
        }
    }

    private void throwRustCodeExecutorServiceNonAvailableException() {
        throw new RustCodeExecutorServiceNonAvailableException(
                taskSolutionExceptionMessageUtil.formRustCodeExecutorServiceNonAvailableException());
    }

    public void checkServiceAlive() {
        try {
            codeExecutorServiceProvider.checkServiceAlive();
        } catch (InterruptedException | ExecutionException e) {
            throwRustCodeExecutorServiceNonAvailableException();
        }
    }

    private void setTaskSolutionInfoAfterChecking(TaskSolution taskSolution, ExecutionResponse executionResponse) {
        if (executionResponse.getStatus() != ExecutionStatus.ALREADY_TEST) {
            taskSolution.setExecutionStatus(executionResponse.getStatus());
            taskSolution.setErrorOutput(executionResponse.getErrorOutput());
            copyExecutionResponseTestToTaskSolutionTests(taskSolution, executionResponse);
        }
    }

    private void copyExecutionResponseTestToTaskSolutionTests(TaskSolution taskSolution,
                                                              ExecutionResponse executionResponse) {
        ExecutionTest executionTest = ExecutionTest.getEmptyExecutionTest(executionResponse.getStatus());
        for (int i = 0; i < taskSolution.getTestsInfos().size(); i++) {
            if (executionResponse.getTests().size() != 0) {
                executionTest = executionResponse.getTests().get(i);
            }
            setExecutionTestInfo(taskSolution.getTestsInfos().get(i), executionTest);
        }
    }

    private void setExecutionTestInfo(TestInfo testInfo, ExecutionTest executionTest) {
        testInfo.setExecutionStatus(executionTest.getStatus());
        testInfo.setErrorOutput(executionTest.getErrorOutput());
        testInfo.setMemoryKb(executionTest.getMemoryKb());
        testInfo.setTimeMs(executionTest.getTimeMs());
        testInfo.setExecutionOutput(getPreparedExecutionOutput(executionTest.getOutput()));
        testInfo.setTestPassed(testInfo.getTaskTest().getOutput()
                .equals(testInfo.getExecutionOutput()));
    }

    private String getPreparedExecutionOutput(String executionOutput) {
        if (executionOutput.matches(".*\\n$")) {
            return executionOutput.substring(0, executionOutput.length() - 1);
        }
        return executionOutput;
    }

    public TaskSolution getTaskSolutionByConditionChecking(
            Authentication authentication, ParticipantEventConditionChecker checker, String id) {
        User participant = userService.getUserByAuthentication(authentication);
        Event event = eventService.getEventByTaskSolutionId(id);
        checker.checkEventForCondition(event, participant);
        return getTaskSolutionByIdAndParticipant(id, participant);
    }
}