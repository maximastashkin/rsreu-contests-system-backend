package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.response;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ExecutionStatus;

import java.lang.reflect.Type;

@Component
public class ExecutionStatusDeserializer implements JsonDeserializer<ExecutionStatus> {
    @Override
    public ExecutionStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return ExecutionStatus.fromRustAnalogue(json.getAsJsonPrimitive().getAsString());
    }
}
