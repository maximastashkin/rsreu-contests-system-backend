package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution;

import lombok.Builder;
import lombok.Data;
import ru.rsreu.contests_system.api.task.TaskTest;

@Builder
@Data
public class TestInfo {
    private TaskTest taskTest;

    private String executionOutput;

    private boolean isTestPassed;

    private long memoryKb;

    private long timeMs;

    @Builder.Default
    private ExecutionStatus executionStatus = ExecutionStatus.NO_SOLUTION;

    private String errorOutput;

    public void setOnCheckingStatus() {
        executionOutput = "";
        isTestPassed = false;
        memoryKb = 0;
        timeMs = 0;
        executionStatus = ExecutionStatus.ON_CHECKING;
        errorOutput = "";
    }
}
