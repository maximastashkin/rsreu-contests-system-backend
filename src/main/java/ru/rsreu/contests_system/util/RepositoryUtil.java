package ru.rsreu.contests_system.util;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationPipeline;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class RepositoryUtil {
    public void addOperationsFromPipeline(AggregationPipeline source, AggregationPipeline destination) {
        source.getOperations().forEach(destination::add);
    }

    public AggregationPipeline getPaginationPipeline(Pageable pageable) {
        return new AggregationPipeline()
                .add(skip(pageable.getOffset()))
                .add(limit(pageable.getPageSize()));
    }

    public <T> Optional<T> getOptionalByQueryResults(List<T> results) {
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Criteria getCriteriaForFilterArrayById(ObjectId id, String iteratorName) {
        return where(iteratorName + "._id").is(id);
    }

    public AggregationOperation getMatchByCompletedOperation(boolean completed) {
        return match(where("participantsInfos").elemMatch(where("factEndDateTime").exists(completed)));
    }

    public MatchOperation getEntityByIdMatchOperation(ObjectId id) {
        return match(getByIdCriteria(id));
    }

    public Criteria getByIdCriteria(ObjectId id) {
        return where("_id").is(id);
    }

    @SuppressWarnings("SameParameterValue")
    public BooleanOperators.Not getNotOperationForFilterCondForFieldExistChecking(String field) {
        return BooleanOperators.Not.not(field);
    }
}
