package ru.rsreu.contests_system.api.organization.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.And;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.Or;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Gte;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Lte;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import ru.rsreu.contests_system.api.organization.event.Event;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter.filter;

@AllArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Event> getAllActualEvents(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        ProjectionOperation filterProjection = Aggregation.project()
                .and(getActualEventsFilter(now)).as("events");
        Aggregation aggregation = Aggregation.newAggregation(
                filterProjection,
                Aggregation.unwind("events"),
                Aggregation.replaceRoot("events"),
                Aggregation.sort(Sort.Direction.ASC, "startDateTime"),
                Aggregation.skip(pageable.getOffset()),
                Aggregation.limit(pageable.getPageSize()));
        return mongoTemplate.aggregate(aggregation, "organizations", Event.class).getMappedResults();
    }

    private ArrayOperators.Filter getActualEventsFilter(LocalDateTime now) {
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
}
