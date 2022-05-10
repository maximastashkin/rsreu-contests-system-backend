package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.supported_languages;

import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ProgrammingLanguage;

public record SupportedLanguagesResponse(
        ProgrammingLanguage programmingLanguage,
        String stringRepresentation) {
}
