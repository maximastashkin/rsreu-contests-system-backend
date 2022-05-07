package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ExecutionStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ExecutionResponse {
    private ExecutionStatus status;

    @Builder.Default
    private List<ExecutionTest> tests = new ArrayList<>();

    @SerializedName("stderr")
    @Builder.Default
    private String errorOutput = "";

    public static ExecutionResponse getCheckingFailExecutionResponse() {
        return ExecutionResponse.builder()
                .status(ExecutionStatus.CHECKING_FAIL)
                .tests(new ArrayList<>()).build();
    }
}
