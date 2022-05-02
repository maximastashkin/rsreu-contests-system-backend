package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ExecutionStatus;

@Data
@Builder
public class ExecutionTest {
    @SerializedName("time")
    long timeMs;

    @SerializedName("memory")
    long memoryKb;

    @SerializedName("stdout")
    String output;

    ExecutionStatus status;

    @SerializedName("stderr")
    String errorOutput;

    public static ExecutionTest getEmptyExecutionTest(ExecutionStatus executionStatus) {
        return ExecutionTest.builder()
                .timeMs(0)
                .memoryKb(0)
                .output("")
                .status(executionStatus)
                .errorOutput("")
                .build();
    }
}
