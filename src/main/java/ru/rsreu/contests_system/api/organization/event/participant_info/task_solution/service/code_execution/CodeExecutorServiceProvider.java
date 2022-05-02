package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ExecutionStatus;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.ProgrammingLanguage;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.request.ExecutionRequest;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.response.ExecutionResponse;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.response.ExecutionStatusDeserializer;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.request.ProgrammingLanguageSerializer;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
@Data
public class CodeExecutorServiceProvider {
    private final BoundRequestBuilder executeRequestBuilder;
    private final ProgrammingLanguageSerializer programmingLanguageSerializer;
    private final ExecutionStatusDeserializer executionStatusDeserializer;

    private Gson gson;

    @PostConstruct
    public void initGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ProgrammingLanguage.class, programmingLanguageSerializer);
        gsonBuilder.registerTypeAdapter(ExecutionStatus.class, executionStatusDeserializer);
        gson = gsonBuilder.create();
    }

    @Async
    public CompletableFuture<ExecutionResponse> asyncExecuteTaskSolution(TaskSolution taskSolution) {
        ExecutionRequest executionRequest = ExecutionRequest.from(taskSolution);
        ListenableFuture<ExecutionResponse> whenResponse = executeRequestBuilder.setBody(gson.toJson(executionRequest))
                .execute(new AsyncCompletionHandler<>() {
                    @Override
                    public ExecutionResponse onCompleted(Response response) {
                        return gson.fromJson(response.getResponseBody(), ExecutionResponse.class);
                    }
                });
        return whenResponse.toCompletableFuture();
    }
}