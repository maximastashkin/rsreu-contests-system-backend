package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution;

import com.google.gson.Gson;
import lombok.Data;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.request.ExecutionRequest;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.service.code_execution.model.response.ExecutionResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@SuppressWarnings("ClassCanBeRecord")
@Service
@Data
public class CodeExecutorServiceProvider {
    private final BoundRequestBuilder executeRequestBuilder;

    private final BoundRequestBuilder aliveRequestBuilder;

    private final Gson gson;

    public CodeExecutorServiceProvider(@Qualifier("executorRequestBuilder") BoundRequestBuilder executeRequestBuilder,
                                       @Qualifier("aliveRequestBuilder") BoundRequestBuilder aliveRequestBuilder,
                                       Gson gson) {
        this.executeRequestBuilder = executeRequestBuilder;
        this.aliveRequestBuilder = aliveRequestBuilder;
        this.gson = gson;
    }

    @Async
    public CompletableFuture<ExecutionResponse> asyncExecuteTaskSolution(TaskSolution taskSolution) {
        ExecutionRequest executionRequest = ExecutionRequest.from(taskSolution);
        ListenableFuture<ExecutionResponse> whenResponse = executeRequestBuilder.setBody(gson.toJson(executionRequest))
                .execute(new AsyncCompletionHandler<>() {
                    @Override
                    public ExecutionResponse onCompleted(Response response) throws InterruptedException {
                        if (response.getStatusCode() != 200) {
                            throw new InterruptedException();
                        }
                        return gson.fromJson(response.getResponseBody(), ExecutionResponse.class);
                    }
                });
        return whenResponse.toCompletableFuture();
    }

    public void checkServiceAlive() throws InterruptedException, ExecutionException {
        if (aliveRequestBuilder.execute().get().getStatusCode() != 200) {
            throw new InterruptedException();
        }
    }
}