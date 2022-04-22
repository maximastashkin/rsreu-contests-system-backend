package ru.rsreu.contests_system.api.organization.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.And;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.Or;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Gte;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Lte;
import org.springframework.data.mongodb.core.query.Criteria;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter.filter;

@AllArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Event> getAllActualEvents(Pageable pageable) {
        Aggregation aggregation = getActualEventsAggregation();
        aggregation.getPipeline()
                .add(Aggregation.skip(pageable.getOffset()))
                .add(Aggregation.limit(pageable.getPageSize()));
        return getEventsByAggregation(aggregation);
    }

    @Override
    public List<Event> getAllUserActualEvents(User user, Pageable pageable) {
        Aggregation aggregation = getActualEventsAggregation();
        aggregation.getPipeline()
                .add(Aggregation.match(
                        Criteria.where("participantsInfos").elemMatch(
                                Criteria.where("participant.$id").is(user.getId()))))
                .add(Aggregation.skip(pageable.getOffset()))
                .add(Aggregation.limit(pageable.getPageSize()));
        return getEventsByAggregation(aggregation);
    }

    private Aggregation getActualEventsAggregation() {
        return Aggregation.newAggregation(Aggregation.project()
                        .and(getActualEventsFilter()).as("events"),
                Aggregation.unwind("events"),
                Aggregation.replaceRoot("events"),
                Aggregation.sort(Sort.Direction.ASC, "startDateTime"));
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
