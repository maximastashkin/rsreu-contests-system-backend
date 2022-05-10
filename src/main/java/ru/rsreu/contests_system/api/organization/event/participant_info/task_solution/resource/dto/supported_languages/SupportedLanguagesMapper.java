package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.dto.supported_languages;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ProgrammingLanguage;

@Component
public class SupportedLanguagesMapper {
    public SupportedLanguagesResponse toResponse(ProgrammingLanguage programmingLanguage) {
        return new SupportedLanguagesResponse(programmingLanguage, programmingLanguage.getStringRepresentation());
    }
}
