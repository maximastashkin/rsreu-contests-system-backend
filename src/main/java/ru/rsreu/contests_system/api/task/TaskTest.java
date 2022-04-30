package ru.rsreu.contests_system.api.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TaskTest {
    private String input;

    private String output;

    @Builder.Default
    private boolean isPublic = false;

    private int weight;
}
