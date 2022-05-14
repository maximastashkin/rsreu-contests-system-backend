package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.repository;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.util.RepositoryUtil;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Component
@AllArgsConstructor
public class TaskSolutionRepositoryImpl implements TaskSolutionRepository {
    private final MongoTemplate mongoTemplate;
    private final RepositoryUtil repositoryUtil;
    private final TaskSolutionRepositoryUtil taskSolutionRepositoryUtil;

    @Override
    public Optional<TaskSolution> findParticipantTaskSolutionById(User participant, ObjectId taskSolutionId) {
        List<TaskSolution> mappedResults = getTasksSolutionsByAggregation(
                newAggregation(
                        taskSolutionRepositoryUtil.getTasksSolutionsByParticipantAndIdPipeline(
                                participant, taskSolutionId).getOperations()));
        return repositoryUtil.getOptionalByQueryResults(mappedResults);
    }

    @Override
    public void setTaskSolutionCheckingInfo(TaskSolution taskSolution) {
        mongoTemplate.updateFirst(
                taskSolutionRepositoryUtil.getOrganizationByTaskSolutionQuery(taskSolution),
                taskSolutionRepositoryUtil.getUpdateForSettingTaskSolutionCheckingInfo(taskSolution),
                Organization.class
        );
    }

    @Override
    public void setTaskSolutionCheckingResultInfo(TaskSolution taskSolution) {
        mongoTemplate.updateFirst(
                taskSolutionRepositoryUtil.getOrganizationByTaskSolutionQuery(taskSolution),
                taskSolutionRepositoryUtil.getUpdateForSettingTaskSolutionCheckingResultInfo(taskSolution),
                Organization.class
        );
    }

    private List<TaskSolution> getTasksSolutionsByAggregation(Aggregation aggregation) {
        return mongoTemplate.aggregate(
                aggregation, "organizations", TaskSolution.class).getMappedResults();
    }
}
