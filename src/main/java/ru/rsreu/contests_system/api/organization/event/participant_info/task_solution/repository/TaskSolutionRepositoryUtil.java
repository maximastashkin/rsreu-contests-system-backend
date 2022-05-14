package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.repository;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.aggregation.AggregationPipeline;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.participant_info.repository.ParticipantInfoRepositoryUtil;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.organization.event.repository.EventRepositoryUtil;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.util.RepositoryUtil;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.replaceRoot;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@SuppressWarnings("ClassCanBeRecord")
@Component
@AllArgsConstructor
public class TaskSolutionRepositoryUtil {
    private final RepositoryUtil repositoryUtil;
    private final EventRepositoryUtil eventRepositoryUtil;
    private final ParticipantInfoRepositoryUtil participantInfoRepositoryUtil;

    public AggregationPipeline getTasksSolutionsByParticipantAndIdPipeline(User participant, ObjectId id) {
        AggregationPipeline pipeline = eventRepositoryUtil.getUnwindEventsPipeline();
        repositoryUtil.addOperationsFromPipeline(
                participantInfoRepositoryUtil.getUnwindParticipantsInfosPipeline(), pipeline);
        pipeline.add(participantInfoRepositoryUtil.getParticipantInfosMatchOperation(participant));
        repositoryUtil.addOperationsFromPipeline(getUnwindTasksSolutionsPipeline(), pipeline);
        pipeline.add(repositoryUtil.getEntityByIdMatchOperation(id));
        return pipeline;
    }

    private AggregationPipeline getUnwindTasksSolutionsPipeline() {
        return new AggregationPipeline().add(unwind("tasksSolutions"))
                .add(replaceRoot("tasksSolutions"));
    }

    public Query getOrganizationByTaskSolutionQuery(TaskSolution taskSolution) {
        return Query.query(where("events.participantsInfos.tasksSolutions").elemMatch(
                repositoryUtil.getByIdCriteria(taskSolution.getId())));
    }

    public Update getUpdateForSettingTaskSolutionCheckingInfo(TaskSolution taskSolution) {
        return new Update()
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].executionStatus",
                        taskSolution.getExecutionStatus())
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].solution", taskSolution.getSolution())
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].testsInfos", taskSolution.getTestsInfos())
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].programmingLanguage",
                        taskSolution.getProgrammingLanguage())
                .filterArray(getCriteriaForFilterParticipantsInfosArrayByTasksSolution(taskSolution, "i"))
                .filterArray(repositoryUtil.getCriteriaForFilterArrayById(taskSolution.getId(), "j"));
    }

    @SuppressWarnings("SameParameterValue")
    private Criteria getCriteriaForFilterParticipantsInfosArrayByTasksSolution(
            TaskSolution taskSolution,
            String iteratorName) {
        return where(iteratorName + ".tasksSolutions").elemMatch(where("_id").is(taskSolution.getId()));
    }

    public Update getUpdateForSettingTaskSolutionCheckingResultInfo(TaskSolution taskSolution) {
        return new Update()
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].executionStatus",
                        taskSolution.getExecutionStatus())
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].errorOutput", taskSolution.getErrorOutput())
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].testsInfos", taskSolution.getTestsInfos())
                .filterArray(getCriteriaForFilterParticipantsInfosArrayByTasksSolution(taskSolution, "i"))
                .filterArray(repositoryUtil.getCriteriaForFilterArrayById(taskSolution.getId(), "j"));
    }
}
