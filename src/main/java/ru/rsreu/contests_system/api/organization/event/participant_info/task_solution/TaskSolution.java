package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ru.rsreu.contests_system.api.task.Task;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TaskSolution {
    @MongoId
    @Builder.Default
    private ObjectId id = new ObjectId();

    @DBRef
    private Task task;

    private String solution;

    private ProgrammingLanguage programmingLanguage;

    @Builder.Default
    private ExecutionStatus executionStatus = ExecutionStatus.NO_SOLUTION;

    private String errorOutput;

    @Builder.Default
    private List<TestInfo> testsInfos = new ArrayList<>();

    public static TaskSolution getTaskSolutionByTask(Task task) {
        List<TestInfo> testInfos = new ArrayList<>();
        task.getTests().forEach(test -> testInfos.add(
                TestInfo.builder().taskTest(test).build()));
        return builder().task(task).testsInfos(testInfos).build();
    }

    public int calculateScore() {
        return testsInfos.stream().mapToInt(
                testInfo -> testInfo.isTestPassed() ? testInfo.getTaskTest().getWeight() : 0).sum();
    }
}
