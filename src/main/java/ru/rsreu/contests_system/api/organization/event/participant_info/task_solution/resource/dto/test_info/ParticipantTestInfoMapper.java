package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.test_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TestInfo;

@Component
public class ParticipantTestInfoMapper {
    public ParticipantTestInfoResponse toResponse(TestInfo testInfo) {
        return new ParticipantTestInfoResponse(
                testInfo.getTaskTest().getInput(),
                testInfo.getTaskTest().getOutput(),
                testInfo.getExecutionStatus(),
                testInfo.getErrorOutput(),
                testInfo.isTestPassed());
    }
}
