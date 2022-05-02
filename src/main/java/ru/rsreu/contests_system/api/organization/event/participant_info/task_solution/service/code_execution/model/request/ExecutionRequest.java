package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.request;

import com.google.gson.annotations.SerializedName;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ProgrammingLanguage;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;

import java.util.List;

public record ExecutionRequest(
        @SerializedName("lang") ProgrammingLanguage language,
        @SerializedName("source") String solution,
        @SerializedName("tests") List<String> testsInputs, long timeout,
        @SerializedName("uuid") String taskSolutionId) {
    public static ExecutionRequest from(TaskSolution taskSolution) {
        return new ExecutionRequest(
                taskSolution.getProgrammingLanguage(),
                taskSolution.getSolution(),
                taskSolution.getTestsInfos().stream().map(
                        testInfo -> testInfo.getTaskTest().getInput()
                ).toList(),
                taskSolution.getTask().getTimeLimitMs(),
                taskSolution.getId().toString());
    }
}
