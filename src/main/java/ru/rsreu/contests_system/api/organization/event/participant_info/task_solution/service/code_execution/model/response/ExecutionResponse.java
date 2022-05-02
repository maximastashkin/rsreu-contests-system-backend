package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ExecutionStatus;

import java.util.List;

@Data
public class ExecutionResponse {
    private ExecutionStatus status;
    private List<ExecutionTest> tests;
    @SerializedName("stderr")
    private String errorOutput;
}
