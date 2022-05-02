package ru.rsreu.contests_system.config.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ExecutionStatus;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ProgrammingLanguage;

@Configuration
@AllArgsConstructor
public class GsonConfiguration {
    private final ProgrammingLanguageSerializer programmingLanguageSerializer;
    private final ExecutionStatusDeserializer executionStatusDeserializer;

    @Bean
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ProgrammingLanguage.class, programmingLanguageSerializer);
        gsonBuilder.registerTypeAdapter(ExecutionStatus.class, executionStatusDeserializer);
        return gsonBuilder.create();
    }
}
