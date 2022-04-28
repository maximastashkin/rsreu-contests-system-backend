package ru.rsreu.contests_system.api.organization.repository;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.And;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.Or;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
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
                getEventByParticipantInfoQuery(participantInfo),
                getUpdateForSettingParticipantInfoStartingProperties(participantInfo),
                Organization.class);
    }

    private Query getEventByParticipantInfoQuery(ParticipantInfo participantInfo) {
        return Query.query(Criteria.where("events")
                .elemMatch(Criteria.where("participantsInfos._id").is(participantInfo.getId())));
    }

    private Update getUpdateForSettingParticipantInfoStartingProperties(ParticipantInfo participantInfo) {
        return new Update()
                .set("events.$.participantsInfos.$[i].tasksSolutions", participantInfo.getTasksSolutions())
                .set("events.$.participantsInfos.$[i].startDateTime", participantInfo.getStartDateTime())
                .set("events.$.participantsInfos.$[i].maxEndDateTime", participantInfo.getMaxEndDateTime())
                .filterArray(getCriteriaForFilterArrayById(participantInfo.getId()));
    }

    private Criteria getCriteriaForFilterArrayById(ObjectId id) {
        return where("i._id").is(id);
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
                getEventByParticipantInfoQuery(participantInfo),
                getUpdateForSettingParticipantInfoFactEndDateTime(participantInfo),
                Organization.class);
    }

    private Update getUpdateForSettingParticipantInfoFactEndDateTime(ParticipantInfo participantInfo) {
        return new Update()
                .set("events.$.participantsInfos.$[i].factEndDateTime", participantInfo.getFactEndDateTime())
                .filterArray(getCriteriaForFilterArrayById(participantInfo.getId()));
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
}