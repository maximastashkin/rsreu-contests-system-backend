package ru.rsreu.contests_system.api.organization.event.repository;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.util.RepositoryUtil;

import java.time.LocalDateTime;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter.filter;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@SuppressWarnings("ClassCanBeRecord")
@Component
@AllArgsConstructor
public class EventRepositoryUtil {
    private final RepositoryUtil repositoryUtil;

    Update getUpdateForParticipantInfoAdding(ParticipantInfo participantInfo) {
        return new Update().addToSet("events.$.participantsInfos", participantInfo);
    }

    Update getUpdateForParticipantInfoRemoving(ParticipantInfo participantInfo) {
        return new Update().pull("events.$.participantsInfos", participantInfo);
    }

    AggregationOperation getEventsSortByStartDateTimeOperation() {
        return sort(Sort.Direction.ASC, "startDateTime");
    }

    ArrayOperators.Filter.ConditionBuilder getEventsFilterBase() {
        return filter("events")
                .as("event");
    }

    ArrayOperators.Filter getActualEventsFilter() {
        LocalDateTime now = LocalDateTime.now();
        return getEventsFilterBase()
                .by(BooleanOperators.Or.or(
                        ComparisonOperators.Gte.valueOf("event.startDateTime").greaterThanEqualToValue(now),
                        BooleanOperators.And.and(
                                ComparisonOperators.Lte.valueOf("event.startDateTime").lessThanEqualToValue(now),
                                ComparisonOperators.Gte.valueOf("event.endDateTime").greaterThanEqualToValue(now)
                        )
                ));
    }

    public AggregationPipeline getUnwindEventsPipeline() {
        return new AggregationPipeline().add(unwind("events")).add(replaceRoot("events"));
    }

    AggregationPipeline getBaseEventAggregationPipelineWithFilter(ArrayOperators.Filter filter) {
        AggregationPipeline pipeline = new AggregationPipeline()
                .add(project().and(filter).as("events"));
        repositoryUtil.addOperationsFromPipeline(getUnwindEventsPipeline(), pipeline);
        return pipeline;
    }

    AggregationPipeline getActualEventsPipeline() {
        return getBaseEventAggregationPipelineWithFilter(
                getActualEventsFilter())
                .add(getEventsSortByStartDateTimeOperation());
    }

    AggregationOperation getParticipantEventsMatchOperation(User participant) {
        return match(
                where("participantsInfos").elemMatch(
                        where("participant.$id").is(participant.getId())));
    }

    Query getOrganizationByEventQuery(Event event) {
        return Query.query(where("events._id").is(event.getId()));
    }

    public AggregationPipeline getEventByIdPipeline(ObjectId eventId) {
        return getBaseEventAggregationPipelineWithFilter(getEventByIdFilter(eventId));
    }

    private ArrayOperators.Filter getEventByIdFilter(ObjectId id) {
        return getEventsFilterBase()
                .by(ComparisonOperators.Eq.valueOf("event._id").equalToValue(id));
    }

    public AggregationPipeline getEventByTaskSolutionIdPipeline(ObjectId taskSolutionId) {
        AggregationPipeline pipeline = getUnwindEventsPipeline();
        pipeline.add(getEventByTaskSolutionIdMatchOperation(taskSolutionId));
        return pipeline;
    }

    private MatchOperation getEventByTaskSolutionIdMatchOperation(ObjectId taskSolutionId) {
        return match(where("participantsInfos.tasksSolutions._id").is(taskSolutionId));
    }

    public Update getUpdateForEventLeaderSetting(Event event, User leader) {
        return new Update()
                .set("events.$[i].eventLeader", leader)
                .filterArray(repositoryUtil.getCriteriaForFilterArrayById(event.getId(), "i"));
    }

    public AggregationPipeline getEventByNamePipeline(String name) {
        return getBaseEventAggregationPipelineWithFilter(getEventByNameFilter(name));
    }

    private ArrayOperators.Filter getEventByNameFilter(String name) {
        return getEventsFilterBase().by(ComparisonOperators.Eq.valueOf("event.name").equalToValue(name));
    }
}
