package ru.rsreu.contests_system.api.organization.event.participant_info.repository;

import lombok.AllArgsConstructor;
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

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Component
@AllArgsConstructor
public class ParticipantInfoRepositoryImpl implements ParticipantInfoRepository {
    private final MongoTemplate mongoTemplate;
    private final ParticipantInfoRepositoryUtil participantInfoRepositoryUtil;
    private final RepositoryUtil repositoryUtil;

    @Override
    public Optional<ParticipantInfo> findParticipantInfoByEventAndParticipant(Event event, User participant) {
        List<ParticipantInfo> mappedResults = getParticipantsInfosByAggregation(
                Aggregation.newAggregation(participantInfoRepositoryUtil
                        .getParticipantInfoByEventAndParticipantPipeline(event, participant).getOperations()));
        return repositoryUtil.getOptionalByQueryResults(mappedResults);
    }

    @Override
    public void addStartingInfoToParticipantInfo(ParticipantInfo participantInfo) {
        mongoTemplate.updateFirst(
                participantInfoRepositoryUtil.getOrganizationByParticipantInfoQuery(participantInfo),
                participantInfoRepositoryUtil.getUpdateForSettingParticipantInfoStartingProperties(participantInfo),
                Organization.class);
    }

    @Override
    public void addFactEndDateTimeToParticipantInfo(ParticipantInfo participantInfo) {
        mongoTemplate.updateFirst(
                participantInfoRepositoryUtil.getOrganizationByParticipantInfoQuery(participantInfo),
                participantInfoRepositoryUtil.getUpdateForSettingParticipantInfoFactEndDateTime(participantInfo),
                Organization.class);
    }

    @Override
    public List<ParticipantInfo> findAllStartedNotCompletedParticipantsInfos() {
        AggregationPipeline pipeline = participantInfoRepositoryUtil.getUnwindEventsPipeline();
        repositoryUtil.addOperationsFromPipeline(
                participantInfoRepositoryUtil.getBaseParticipantInfoAggregationPipelineWithFilter(
                        participantInfoRepositoryUtil.getFilterForAllStartedNotCompletedParticipantsInfos()),
                pipeline);
        return getParticipantsInfosByAggregation(newAggregation(pipeline.getOperations()));
    }

    private List<ParticipantInfo> getParticipantsInfosByAggregation(Aggregation aggregation) {
        return mongoTemplate.aggregate(
                aggregation, "organizations", ParticipantInfo.class).getMappedResults();
    }
}
