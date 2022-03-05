package ru.rsreu.contests_system.organization.event.participant_info.task_solution;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ru.rsreu.contests_system.task.Task;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class TaskSolution {
    @MongoId
    @Builder.Default private ObjectId id = new ObjectId();

    @DBRef
    private Task task;

    private String solution;

    private SolutionStatus solutionStatus;

    @Builder.Default private Set<TestInfo> testsInfos = new HashSet<>();

    public void addTestInfo(TestInfo testInfo) {
        testsInfos.add(testInfo);
    }
}
