package ru.rsreu.contests_system.api.organization.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.Or;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.And;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Lte;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Gte;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter.filter;

@AllArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Event> getAllActualEvents() {
        LocalDateTime now = LocalDateTime.now();
        ProjectionOperation projection = Aggregation.project()
                .and(filter("events")
                        .as("event")
                        .by(Or.or(
                                Gte.valueOf("event.startDateTime").greaterThanEqualToValue(now),
                                And.and(
                                        Lte.valueOf("event.startDateTime").lessThanEqualToValue(now),
                                        Gte.valueOf("event.endDateTime").greaterThanEqualToValue(now)
                                )
                        ))).as("events");
        Aggregation aggregation = Aggregation.newAggregation(projection);
        List<Organization> organizations = mongoTemplate.aggregate(aggregation, "organizations", Organization.class).getMappedResults();
        return organizations.stream().map(Organization::getEvents).flatMap(Collection::stream).toList();
    }
}
