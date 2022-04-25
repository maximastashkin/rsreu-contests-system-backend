package ru.rsreu.contests_system.api.organization.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationPipeline;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.And;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.Or;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Gte;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Lte;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter.filter;
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

    private AggregationPipeline getPaginationPipeline(Pageable pageable) {
        return new AggregationPipeline()
                .add(skip(pageable.getOffset()))
                .add(limit(pageable.getPageSize()));
    }

    @Override
    public List<Event> findUserAllActualEvents(User user, Pageable pageable) {
        AggregationPipeline pipeline = getActualEventsAggregationPipeline();
        pipeline.add(getUserEventsMatchOperation(user)).add(getMatchByCompletedOperation(false));
        addOperationsFromPipeline(getPaginationPipeline(pageable), pipeline);
        return getEventsByAggregation(Aggregation.newAggregation(pipeline.getOperations()));
    }

    private AggregationOperation getUserEventsMatchOperation(User user) {
        return match(
                where("participantsInfos").elemMatch(
                        where("participant.$id").is(user.getId())));
    }

    private AggregationOperation getMatchByCompletedOperation(boolean completed) {
        return match(where("participantsInfos").elemMatch(where("completed").is(completed)));
    }

    @Override
    public List<Event> findUserAllCompletedEvents(User user, Pageable pageable) {
        AggregationPipeline pipeline = getUnwindEventsPipeline();
        pipeline.add(getUserEventsMatchOperation(user)).add(getMatchByCompletedOperation(true));
        pipeline.add(getSortByStartDateTimeOperation());
        addOperationsFromPipeline(getPaginationPipeline(pageable), pipeline);
        return getEventsByAggregation(Aggregation.newAggregation(pipeline.getOperations()));
    }

    private AggregationOperation getSortByStartDateTimeOperation() {
        return sort(Sort.Direction.ASC, "startDateTime");
    }

    private AggregationPipeline getUnwindEventsPipeline() {
        AggregationPipeline aggregationPipeline = new AggregationPipeline();
        aggregationPipeline.add(unwind("events"));
        aggregationPipeline.add(replaceRoot("events"));
        return aggregationPipeline;
    }

    private void addOperationsFromPipeline(AggregationPipeline source, AggregationPipeline destination) {
        source.getOperations().forEach(destination::add);
    }

    private AggregationPipeline getActualEventsAggregationPipeline() {
        AggregationPipeline pipeline = new AggregationPipeline()
                .add(project().and(getActualEventsFilter()).as("events"));
        addOperationsFromPipeline(getUnwindEventsPipeline(), pipeline);
        pipeline.add(getSortByStartDateTimeOperation());
        return pipeline;
    }

    private AggregationExpression getActualEventsFilter() {
        LocalDateTime now = LocalDateTime.now();
        return filter("events")
                .as("event")
                .by(Or.or(
                        Gte.valueOf("event.startDateTime").greaterThanEqualToValue(now),
                        And.and(
                                Lte.valueOf("event.startDateTime").lessThanEqualToValue(now),
                                Gte.valueOf("event.endDateTime").greaterThanEqualToValue(now)
                        )
                ));
    }

    private List<Event> getEventsByAggregation(Aggregation aggregation) {
        return mongoTemplate.aggregate(aggregation, "organizations", Event.class).getMappedResults();
    }
}
