package ru.rsreu.contests_system.api.organization.repository;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.And;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.Not;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.Or;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter.filter;
import static org.springframework.data.mongodb.core.aggregation.ComparisonOperators.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@AllArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Event> findAllActualEvents(Pageable pageable) {
        AggregationPipeline pipeline = getActualEventsAggregationPipeline();
        addOperationsFromPipeline(getPaginationPipeline(pageable), pipeline);
        return getEventsByAggregation(Aggregation.newAggregation(pipeline.getOperations()));
    }

    @Override
    public List<Event> findUserAllActualEvents(User user, Pageable pageable) {
        AggregationPipeline pipeline = getActualEventsAggregationPipeline();
        pipeline.add(getUserEventsMatchOperation(user)).add(getMatchByCompletedOperation(false));
        addOperationsFromPipeline(getPaginationPipeline(pageable), pipeline);
        return getEventsByAggregation(Aggregation.newAggregation(pipeline.getOperations()));
    }

    @Override
    public List<Event> findUserAllCompletedEvents(User user, Pageable pageable) {
        AggregationPipeline pipeline = getUnwindEventsPipeline();
        pipeline.add(getUserEventsMatchOperation(user)).add(getMatchByCompletedOperation(true));
        pipeline.add(getSortByStartDateTimeOperation());
        addOperationsFromPipeline(getPaginationPipeline(pageable), pipeline);
        return getEventsByAggregation(Aggregation.newAggregation(pipeline.getOperations()));
    }

    @Override
    public void addParticipantInfoToEvent(ParticipantInfo participantInfo, Event event) {
        mongoTemplate.updateFirst(
                getOrganizationByEventQuery(event),
                new Update().addToSet("events.$.participantsInfos", participantInfo),
                Organization.class);
    }

    private Query getOrganizationByEventQuery(Event event) {
        return Query.query(Criteria.where("events._id").is(event.getId()));
    }

    @Override
    public Optional<Event> findEventById(ObjectId eventId) {
        List<Event> mappedResults = getEventsByAggregation(
                Aggregation.newAggregation(getEventByIdAggregationPipeline(eventId).getOperations()));
        return mappedResults.isEmpty() ? Optional.empty() : Optional.of(mappedResults.get(0));
    }

    @Override
    public void removeParticipantInfoFromEvent(ParticipantInfo participantInfo, Event event) {
        mongoTemplate.updateFirst(
                getOrganizationByEventQuery(event),
                new Update().pull("events.$.participantsInfos", participantInfo),
                Organization.class);
    }

    @Override
    public void addStartingInfoToParticipantInfo(ParticipantInfo participantInfo) {
        mongoTemplate.updateFirst(
                getOrganizationByParticipantInfoQuery(participantInfo),
                getUpdateForSettingParticipantInfoStartingProperties(participantInfo),
                Organization.class);
    }

    private Query getOrganizationByParticipantInfoQuery(ParticipantInfo participantInfo) {
        return Query.query(where("events")
                .elemMatch(where("participantsInfos._id").is(participantInfo.getId())));
    }

    private Update getUpdateForSettingParticipantInfoStartingProperties(ParticipantInfo participantInfo) {
        return new Update()
                .set("events.$.participantsInfos.$[i].tasksSolutions", participantInfo.getTasksSolutions())
                .set("events.$.participantsInfos.$[i].startDateTime", participantInfo.getStartDateTime())
                .set("events.$.participantsInfos.$[i].maxEndDateTime", participantInfo.getMaxEndDateTime())
                .filterArray(getCriteriaForFilterArrayById(participantInfo.getId(), "i"));
    }

    private Criteria getCriteriaForFilterArrayById(ObjectId id, String iteratorName) {
        return where(iteratorName + "._id").is(id);
    }

    @Override
    public Optional<ParticipantInfo> findParticipantInfoByEventAndParticipant(Event event, User participant) {
        List<ParticipantInfo> mappedResults = getParticipantsInfosByAggregation(
                Aggregation.newAggregation(
                        getParticipantInfoByEventAndParticipantPipeline(event, participant).getOperations()));
        return mappedResults.isEmpty() ? Optional.empty() : Optional.of(mappedResults.get(0));
    }

    @Override
    public void addFactEndDateTimeToParticipantInfo(ParticipantInfo participantInfo) {
        mongoTemplate.updateFirst(
                getOrganizationByParticipantInfoQuery(participantInfo),
                getUpdateForSettingParticipantInfoFactEndDateTime(participantInfo),
                Organization.class);
    }

    @Override
    public Optional<Event> findEventByTaskSolutionId(ObjectId taskSolutionId) {
        List<Event> mappedResults = getEventsByAggregation(
                Aggregation.newAggregation(getEventByTaskSolutionIdPipeline(taskSolutionId).getOperations()));
        return mappedResults.isEmpty() ? Optional.empty() : Optional.of(mappedResults.get(0));
    }

    @Override
    public Optional<TaskSolution> findParticipantTaskSolutionById(User participant, ObjectId taskSolutionId) {
        List<TaskSolution> mappedResults = getTasksSolutionsByAggregation(
                newAggregation(
                        getTasksSolutionsByParticipantAndIdPipeline(participant, taskSolutionId).getOperations()));
        return mappedResults.isEmpty() ? Optional.empty() : Optional.of(mappedResults.get(0));
    }

    @Override
    public void setTaskSolutionCheckingInfo(TaskSolution taskSolution) {
        mongoTemplate.updateFirst(
                getOrganizationByTaskSolutionQuery(taskSolution),
                getUpdateForSettingTaskSolutionCheckingInfo(taskSolution),
                Organization.class
        );
    }

    @Override
    public void setTaskSolutionCheckingResultInfo(TaskSolution taskSolution) {
        mongoTemplate.updateFirst(
                getOrganizationByTaskSolutionQuery(taskSolution),
                getUpdateForSettingTaskSolutionCheckingResultInfo(taskSolution),
                Organization.class
        );
    }

    @Override
    public List<ParticipantInfo> findAllNotCompletedParticipantsInfos() {
        AggregationPipeline pipeline = getUnwindEventsPipeline();
        addOperationsFromPipeline(
                getBaseParticipantInfoAggregationPipelineWithFilter(getFilterForAllNotCompletedParticipantsInfos()),
                pipeline);
        return getParticipantsInfosByAggregation(newAggregation(pipeline.getOperations()));
    }

    private AggregationPipeline getBaseParticipantInfoAggregationPipelineWithFilter(ArrayOperators.Filter filter) {
        AggregationPipeline pipeline = new AggregationPipeline()
                .add(project().and(filter).as("participantsInfos"));
        addOperationsFromPipeline(getUnwindParticipantsInfosPipeline(), pipeline);
        return pipeline;
    }

    private ArrayOperators.Filter getFilterForAllNotCompletedParticipantsInfos() {
        return filter("participantsInfos")
                .as("info")
                .by(getNotOperationForFilterCondForFieldExistChecking("info.factEndDateTime"));
    }

    @SuppressWarnings("SameParameterValue")
    private Not getNotOperationForFilterCondForFieldExistChecking(String field) {
        return Not.not(field);
    }

    private Query getOrganizationByTaskSolutionQuery(TaskSolution taskSolution) {
        return Query.query(where("events.participantsInfos.tasksSolutions").elemMatch(
                getByIdCriteria(taskSolution.getId())));
    }

    private Update getUpdateForSettingTaskSolutionCheckingInfo(TaskSolution taskSolution) {
        return new Update()
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].executionStatus",
                        taskSolution.getExecutionStatus())
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].solution", taskSolution.getSolution())
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].testsInfos", taskSolution.getTestsInfos())
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].programmingLanguage",
                        taskSolution.getProgrammingLanguage())
                .filterArray(getCriteriaForFilterParticipantsInfosArrayByTasksSolution(taskSolution, "i"))
                .filterArray(getCriteriaForFilterArrayById(taskSolution.getId(), "j"));
    }

    @SuppressWarnings("SameParameterValue")
    private Criteria getCriteriaForFilterParticipantsInfosArrayByTasksSolution(TaskSolution taskSolution,
                                                                               String iteratorName) {
        return where(iteratorName + ".tasksSolutions").elemMatch(where("_id").is(taskSolution.getId()));
    }

    private Update getUpdateForSettingTaskSolutionCheckingResultInfo(TaskSolution taskSolution) {
        return new Update()
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].executionStatus",
                        taskSolution.getExecutionStatus())
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].errorOutput", taskSolution.getErrorOutput())
                .set("events.$.participantsInfos.$[i].tasksSolutions.$[j].testsInfos", taskSolution.getTestsInfos())
                .filterArray(getCriteriaForFilterParticipantsInfosArrayByTasksSolution(taskSolution, "i"))
                .filterArray(getCriteriaForFilterArrayById(taskSolution.getId(), "j"));
    }

    private AggregationPipeline getTasksSolutionsByParticipantAndIdPipeline(User participant, ObjectId id) {
        AggregationPipeline pipeline = getUnwindEventsPipeline();
        addOperationsFromPipeline(getUnwindParticipantsInfosPipeline(), pipeline);
        pipeline.add(getParticipantInfosMatchOperation(participant));
        addOperationsFromPipeline(getUnwindTasksSolutionsPipeline(), pipeline);
        pipeline.add(getEntityByIdMatchOperation(id));
        return pipeline;
    }

    private MatchOperation getEntityByIdMatchOperation(ObjectId id) {
        return match(getByIdCriteria(id));
    }

    private Criteria getByIdCriteria(ObjectId id) {
        return where("_id").is(id);
    }

    private AggregationPipeline getUnwindTasksSolutionsPipeline() {
        return new AggregationPipeline().add(unwind("tasksSolutions"))
                .add(replaceRoot("tasksSolutions"));
    }

    private AggregationPipeline getEventByTaskSolutionIdPipeline(ObjectId taskSolutionId) {
        AggregationPipeline pipeline = getUnwindEventsPipeline();
        pipeline.add(getEventByTaskSolutionIdMatchOperation(taskSolutionId));
        return pipeline;
    }

    private MatchOperation getEventByTaskSolutionIdMatchOperation(ObjectId taskSolutionId) {
        return match(where("participantsInfos.tasksSolutions._id").is(taskSolutionId));
    }

    private Update getUpdateForSettingParticipantInfoFactEndDateTime(ParticipantInfo participantInfo) {
        return new Update()
                .set("events.$.participantsInfos.$[i].factEndDateTime", participantInfo.getFactEndDateTime())
                .filterArray(getCriteriaForFilterArrayById(participantInfo.getId(), "i"));
    }

    private AggregationPipeline getParticipantInfoByEventAndParticipantPipeline(Event event, User participant) {
        AggregationPipeline pipeline = getEventByIdAggregationPipeline(event.getId());
        addOperationsFromPipeline(getUnwindParticipantsInfosPipeline(), pipeline);
        pipeline.add(getParticipantInfosMatchOperation(participant));
        return pipeline;
    }

    private MatchOperation getParticipantInfosMatchOperation(User participant) {
        return match(Criteria.where("participant.$id").is(participant.getId()));
    }

    private AggregationPipeline getUnwindParticipantsInfosPipeline() {
        return new AggregationPipeline().add(unwind("participantsInfos"))
                .add(replaceRoot("participantsInfos"));
    }

    private AggregationPipeline getActualEventsAggregationPipeline() {
        return getBaseEventAggregationPipelineWithFilter(getActualEventsFilter())
                .add(getSortByStartDateTimeOperation());
    }

    private void addOperationsFromPipeline(AggregationPipeline source, AggregationPipeline destination) {
        source.getOperations().forEach(destination::add);
    }

    private AggregationPipeline getUnwindEventsPipeline() {
        return new AggregationPipeline().add(unwind("events")).add(replaceRoot("events"));
    }

    private AggregationPipeline getPaginationPipeline(Pageable pageable) {
        return new AggregationPipeline()
                .add(skip(pageable.getOffset()))
                .add(limit(pageable.getPageSize()));
    }

    private AggregationPipeline getEventByIdAggregationPipeline(ObjectId eventId) {
        return getBaseEventAggregationPipelineWithFilter(getEventByIdFilter(eventId));
    }

    private AggregationPipeline getBaseEventAggregationPipelineWithFilter(ArrayOperators.Filter filter) {
        AggregationPipeline pipeline = new AggregationPipeline()
                .add(project().and(filter).as("events"));
        addOperationsFromPipeline(getUnwindEventsPipeline(), pipeline);
        return pipeline;
    }

    private ArrayOperators.Filter getActualEventsFilter() {
        LocalDateTime now = LocalDateTime.now();
        return getEventsFilterBase()
                .by(Or.or(
                        Gte.valueOf("event.startDateTime").greaterThanEqualToValue(now),
                        And.and(
                                Lte.valueOf("event.startDateTime").lessThanEqualToValue(now),
                                Gte.valueOf("event.endDateTime").greaterThanEqualToValue(now)
                        )
                ));
    }

    private ArrayOperators.Filter getEventByIdFilter(ObjectId id) {
        return getEventsFilterBase()
                .by(Eq.valueOf("event._id").equalToValue(id));
    }

    private ArrayOperators.Filter.ConditionBuilder getEventsFilterBase() {
        return filter("events")
                .as("event");
    }

    private AggregationOperation getSortByStartDateTimeOperation() {
        return sort(Sort.Direction.ASC, "startDateTime");
    }

    private AggregationOperation getUserEventsMatchOperation(User user) {
        return match(
                where("participantsInfos").elemMatch(
                        where("participant.$id").is(user.getId())));
    }

    private AggregationOperation getMatchByCompletedOperation(boolean completed) {
        return match(where("participantsInfos").elemMatch(where("factEndDateTime").exists(completed)));
    }

    private List<Event> getEventsByAggregation(Aggregation aggregation) {
        return mongoTemplate.aggregate(aggregation, "organizations", Event.class).getMappedResults();
    }

    private List<ParticipantInfo> getParticipantsInfosByAggregation(Aggregation aggregation) {
        return mongoTemplate.aggregate(
                aggregation, "organizations", ParticipantInfo.class).getMappedResults();
    }

    private List<TaskSolution> getTasksSolutionsByAggregation(Aggregation aggregation) {
        return mongoTemplate.aggregate(
                aggregation, "organizations", TaskSolution.class).getMappedResults();
    }
}