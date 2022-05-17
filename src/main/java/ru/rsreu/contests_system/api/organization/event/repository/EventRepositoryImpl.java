package ru.rsreu.contests_system.api.organization.event.repository;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationPipeline;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.util.RepositoryUtil;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class EventRepositoryImpl implements EventRepository {
    private final MongoTemplate mongoTemplate;
    private final EventRepositoryUtil eventRepositoryUtil;
    private final RepositoryUtil repositoryUtil;

    @Override
    public Optional<Event> findEventById(ObjectId eventId) {
        List<Event> mappedResults = getEventsByAggregation(
                Aggregation.newAggregation(eventRepositoryUtil.getEventByIdPipeline(eventId)
                        .getOperations()));
        return repositoryUtil.getOptionalByQueryResults(mappedResults);
    }

    @Override
    public List<Event> findAllActualEvents(Pageable pageable) {
        AggregationPipeline pipeline = eventRepositoryUtil.getActualEventsPipeline();
        repositoryUtil.addOperationsFromPipeline(repositoryUtil.getPaginationPipeline(pageable), pipeline);
        return getEventsByAggregation(Aggregation.newAggregation(pipeline.getOperations()));
    }

    @Override
    public List<Event> findUserAllActualEvents(User user, Pageable pageable) {
        AggregationPipeline pipeline = eventRepositoryUtil.getActualEventsPipeline();
        pipeline.add(eventRepositoryUtil.getParticipantEventsMatchOperation(user))
                .add(repositoryUtil.getMatchByCompletedOperation(false));
        repositoryUtil.addOperationsFromPipeline(repositoryUtil.getPaginationPipeline(pageable), pipeline);
        return getEventsByAggregation(Aggregation.newAggregation(pipeline.getOperations()));
    }

    @Override
    public List<Event> findUserAllCompletedEvents(User user, Pageable pageable) {
        AggregationPipeline pipeline = eventRepositoryUtil.getUnwindEventsPipeline();
        pipeline.add(eventRepositoryUtil.getParticipantEventsMatchOperation(user))
                .add(repositoryUtil.getMatchByCompletedOperation(true));
        pipeline.add(eventRepositoryUtil.getEventsSortByStartDateTimeOperation());
        repositoryUtil.addOperationsFromPipeline(repositoryUtil.getPaginationPipeline(pageable), pipeline);
        return getEventsByAggregation(Aggregation.newAggregation(pipeline.getOperations()));
    }

    @Override
    public void addParticipantInfoToEvent(ParticipantInfo participantInfo, Event event) {
        mongoTemplate.updateFirst(
                eventRepositoryUtil.getOrganizationByEventQuery(event),
                eventRepositoryUtil.getUpdateForParticipantInfoAdding(participantInfo),
                Organization.class);
    }

    @Override
    public void removeParticipantInfoFromEvent(ParticipantInfo participantInfo, Event event) {
        mongoTemplate.updateFirst(
                eventRepositoryUtil.getOrganizationByEventQuery(event),
                eventRepositoryUtil.getUpdateForParticipantInfoRemoving(participantInfo),
                Organization.class);
    }

    @Override
    public Optional<Event> findEventByTaskSolutionId(ObjectId taskSolutionId) {
        List<Event> mappedResults = getEventsByAggregation(
                Aggregation.newAggregation(eventRepositoryUtil
                        .getEventByTaskSolutionIdPipeline(taskSolutionId).getOperations()));
        return repositoryUtil.getOptionalByQueryResults(mappedResults);
    }

    @Override
    public void setEventLeader(Event event, User leader) {
        mongoTemplate.updateFirst(
                eventRepositoryUtil.getOrganizationByEventQuery(event),
                eventRepositoryUtil.getUpdateForEventLeaderSetting(event, leader),
                Organization.class
        );
    }

    @Override
    public Optional<Event> findEventByName(String name) {
        List<Event> mappedResults = getEventsByAggregation(
                Aggregation.newAggregation(eventRepositoryUtil.getEventByNamePipeline(name).getOperations()));
        return repositoryUtil.getOptionalByQueryResults(mappedResults);
    }

    private List<Event> getEventsByAggregation(Aggregation aggregation) {
        return mongoTemplate.aggregate(aggregation, "organizations", Event.class).getMappedResults();
    }
}
