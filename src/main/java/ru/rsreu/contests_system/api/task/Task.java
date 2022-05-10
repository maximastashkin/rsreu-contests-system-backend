package ru.rsreu.contests_system.api.task;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "tasks")
@Data
@Builder
public class Task {
    @MongoId
    private ObjectId id;

    private String title;

    private String text;

    private long memoryLimitKb;

    private long timeLimitMs;

    @Builder.Default
    private List<TaskTest> tests = new ArrayList<>();

    public int calculateMaxScore() {
        return tests.stream().mapToInt(TaskTest::getWeight).sum();
    }
}
