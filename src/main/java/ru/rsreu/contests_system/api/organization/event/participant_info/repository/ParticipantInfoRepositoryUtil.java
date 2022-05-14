package ru.rsreu.contests_system.api.organization.event.participant_info.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.AggregationPipeline;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.organization.event.repository.EventRepositoryUtil;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.util.RepositoryUtil;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter.filter;
import static org.springframework.data.mongodb.core.aggregation.BooleanOperators.And.and;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@SuppressWarnings("ClassCanBeRecord")
@Component
@AllArgsConstructor
public class ParticipantInfoRepositoryUtil {
    private final RepositoryUtil repositoryUtil;
    private final EventRepositoryUtil eventRepositoryUtil;

    public AggregationPipeline getParticipantInfoByEventAndParticipantPipeline(Event event, User participant) {
        AggregationPipeline pipeline = eventRepositoryUtil.getEventByIdAggregationPipeline(event.getId());
        repositoryUtil.addOperationsFromPipeline(getUnwindParticipantsInfosPipeline(), pipeline);
        pipeline.add(getParticipantInfosMatchOperation(participant));
        return pipeline;
    }

    public AggregationPipeline getUnwindParticipantsInfosPipeline() {
        return new AggregationPipeline().add(unwind("participantsInfos"))
                .add(replaceRoot("participantsInfos"));
    }

    public MatchOperation getParticipantInfosMatchOperation(User participant) {
        return match(Criteria.where("participant.$id").is(participant.getId()));
    }

    public Query getOrganizationByParticipantInfoQuery(ParticipantInfo participantInfo) {
        return Query.query(where("events")
                .elemMatch(where("participantsInfos._id").is(participantInfo.getId())));
    }

    public Update getUpdateForSettingParticipantInfoStartingProperties(ParticipantInfo participantInfo) {
        return new Update()
                .set("events.$.participantsInfos.$[i].tasksSolutions", participantInfo.getTasksSolutions())
                .set("events.$.participantsInfos.$[i].startDateTime", participantInfo.getStartDateTime())
                .set("events.$.participantsInfos.$[i].maxEndDateTime", participantInfo.getMaxEndDateTime())
                .filterArray(repositoryUtil.getCriteriaForFilterArrayById(participantInfo.getId(), "i"));
    }

    public Update getUpdateForSettingParticipantInfoFactEndDateTime(ParticipantInfo participantInfo) {
        return new Update()
                .set("events.$.participantsInfos.$[i].factEndDateTime", participantInfo.getFactEndDateTime())
                .filterArray(repositoryUtil.getCriteriaForFilterArrayById(participantInfo.getId(), "i"));
    }

    public AggregationPipeline getUnwindEventsPipeline() {
        return eventRepositoryUtil.getUnwindEventsPipeline();
    }

    public AggregationPipeline getBaseParticipantInfoAggregationPipelineWithFilter(ArrayOperators.Filter filter) {
        AggregationPipeline pipeline = new AggregationPipeline()
                .add(project().and(filter).as("participantsInfos"));
        repositoryUtil.addOperationsFromPipeline(getUnwindParticipantsInfosPipeline(), pipeline);
        return pipeline;
    }

    public ArrayOperators.Filter getFilterForAllStartedNotCompletedParticipantsInfos() {
        return filter("participantsInfos")
                .as("info")
                .by(and(
                        repositoryUtil.getExistsAggregationExpression("info.factEndDateTime", false),
                        repositoryUtil.getExistsAggregationExpression("info.startDateTime", true)));
    }
}
