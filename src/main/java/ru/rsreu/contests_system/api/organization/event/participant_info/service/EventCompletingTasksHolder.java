package ru.rsreu.contests_system.api.organization.event.participant_info.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;

import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
@Component
public class EventCompletingTasksHolder {
    private final ThreadPoolTaskScheduler scheduler;
    private final ZoneOffset zoneOffset;
    private final Map<ParticipantInfo, ScheduledFuture<?>> tasks = new HashMap<>();

    public void addTask(CompleteEventRunnableTask task) {
        tasks.put(task.participantInfo(),
                scheduler.schedule(task, task.participantInfo().getMaxEndDateTime().toInstant(zoneOffset)));
    }

    public void cancelTaskByParticipantInfo(ParticipantInfo participantInfo) {
        tasks.get(participantInfo).cancel(false);
    }
}
