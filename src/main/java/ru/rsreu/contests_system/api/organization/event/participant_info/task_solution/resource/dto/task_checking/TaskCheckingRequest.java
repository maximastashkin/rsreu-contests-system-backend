package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.task_checking;

import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ProgrammingLanguage;

import javax.validation.constraints.NotBlank;

public record TaskCheckingRequest(
        @NotBlank
        String solution,
        ProgrammingLanguage language) {
}
