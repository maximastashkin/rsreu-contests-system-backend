package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.exception;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.user.User;

@Component
public class TaskSolutionExceptionsMessagesUtil {
    public String formTaskSolutionForParticipantNotFoundException(User participant, String taskSolutionId) {
        return String.format("Participant with email:%s  is not related to TaskSolution with id:%s",
                participant.getEmail(), taskSolutionId);
    }

    public String formRustCodeExecutorServiceNonAvailableException() {
        return "Now code executor service is not available";
    }
}
