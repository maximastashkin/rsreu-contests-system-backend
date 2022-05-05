package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.completed_task_solution_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TestInfo;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.performed_task_solution_info.PerformedTaskSolutionInfoMapper;

import java.util.List;

@Component
public record CompletedTaskSolutionInfoMapper(PerformedTaskSolutionInfoMapper taskSolutionInfoMapper) {
    public CompletedTaskSolutionInfoResponse toResponse(TaskSolution taskSolution) {
        ScoreWithMaxScore scoreWithMaxScore = calculateScoreWithMaxScore(taskSolution.getTestsInfos());
        return new CompletedTaskSolutionInfoResponse(
                taskSolutionInfoMapper.toResponse(taskSolution),
                scoreWithMaxScore.score,
                scoreWithMaxScore.maxScore
        );
    }

    public ScoreWithMaxScore calculateScoreWithMaxScore(List<TestInfo> testsInfos) {
        return testsInfos.stream()
                .map(testInfo -> new ScoreWithMaxScore(
                        testInfo.isTestPassed() ? testInfo.getTaskTest().getWeight() : 0,
                        testInfo.getTaskTest().getWeight()))
                .reduce(new ScoreWithMaxScore(0, 0), (previous, current) ->
                        new ScoreWithMaxScore(previous.score + current.score,
                                previous.maxScore + current.maxScore));
    }

    private record ScoreWithMaxScore(int score, int maxScore) {
    }
}
