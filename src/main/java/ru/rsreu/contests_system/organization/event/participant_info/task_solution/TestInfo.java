package ru.rsreu.contests_system.organization.event.participant_info.task_solution;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TestInfo {
    private long time;

    private long memory;

    private int score;
}
