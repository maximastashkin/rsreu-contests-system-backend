package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.request;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ProgrammingLanguage;

import java.lang.reflect.Type;

@Component
public class ProgrammingLanguageSerializer implements JsonSerializer<ProgrammingLanguage> {
    @Override
    public JsonElement serialize(ProgrammingLanguage src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString().toLowerCase());
    }
}
