package com.example.managerapp.service;

import com.example.managerapp.model.Task;
import com.example.managerapp.repository.TaskRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final Counter tasksCreated;
    private final Counter tasksCompleted;

    public TaskService(TaskRepository taskRepository, MeterRegistry meterRegistry) {
        this.taskRepository = taskRepository;
        this.tasksCreated = meterRegistry.counter("app.tasks.created");
        this.tasksCompleted = meterRegistry.counter("app.tasks.completed");
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        Task savedTask = taskRepository.save(task);
        tasksCreated.increment();
        return savedTask;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setAssignedTo(updatedTask.getAssignedTo());
                    task.setDeadline(updatedTask.getDeadline());

                    boolean wasNotCompleted = !Boolean.TRUE.equals(task.getCompleted());
                    task.setCompleted(updatedTask.getCompleted());

                    if (Boolean.TRUE.equals(updatedTask.getCompleted()) && wasNotCompleted) {
                        tasksCompleted.increment();
                    }

                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found with id " + id));
    }
}
