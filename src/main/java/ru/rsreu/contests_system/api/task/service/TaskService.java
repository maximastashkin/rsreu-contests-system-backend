package ru.rsreu.contests_system.api.task.service;

import org.springframework.stereotype.Service;
import ru.rsreu.contests_system.api.task.Task;
import ru.rsreu.contests_system.api.task.repository.TaskRepository;

import java.util.List;

@Service
public record TaskService(TaskRepository taskRepository) {
    public void save(Task task) {
        taskRepository.save(task);
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }
}
