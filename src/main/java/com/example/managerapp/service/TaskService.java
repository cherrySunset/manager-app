package com.example.managerapp.service;

import com.example.managerapp.model.Task;
import com.example.managerapp.repository.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final DeadlineRepository deadlineRepository;
    private final ProjectRepository projectRepository;

    private final Counter tasksCreated;
    private final Counter tasksCompleted;

    public TaskService(
            TaskRepository taskRepository,
            UserRepository userRepository,
            DeadlineRepository deadlineRepository,
            ProjectRepository projectRepository,
            MeterRegistry meterRegistry
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.deadlineRepository = deadlineRepository;
        this.projectRepository = projectRepository;

        this.tasksCreated = meterRegistry.counter("app.tasks.created");
        this.tasksCompleted = meterRegistry.counter("app.tasks.completed");

        logger.info("TaskService initialized with task counters.");
    }

    public List<Task> getAllTasks() {
        logger.info("Fetching all tasks");
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        logger.info("Creating task: {}", task);

        if (task.getAssignedTo() != null && task.getAssignedTo().getId() != null) {
            userRepository.findById(task.getAssignedTo().getId()).ifPresent(task::setAssignedTo);
        }

        if (task.getDeadline() != null && task.getDeadline().getId() != null) {
            deadlineRepository.findById(task.getDeadline().getId()).ifPresent(task::setDeadline);
        }

        if (task.getProject() != null && task.getProject().getId() != null) {
            projectRepository.findById(task.getProject().getId()).ifPresent(task::setProject);
        }

        Task savedTask = taskRepository.save(task);
        tasksCreated.increment();
        logger.debug("Task created and saved: {}", savedTask);
        return savedTask;
    }

    public void deleteTask(Long id) {
        logger.info("Deleting task with ID: {}", id);
        taskRepository.deleteById(id);
        logger.debug("Task with ID {} deleted", id);
    }

    public Task updateTask(Long id, Task updatedTask) {
        logger.info("Updating task with ID: {}", id);
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());

                    if (updatedTask.getAssignedTo() != null && updatedTask.getAssignedTo().getId() != null) {
                        userRepository.findById(updatedTask.getAssignedTo().getId()).ifPresent(task::setAssignedTo);
                    }

                    if (updatedTask.getDeadline() != null && updatedTask.getDeadline().getId() != null) {
                        deadlineRepository.findById(updatedTask.getDeadline().getId()).ifPresent(task::setDeadline);
                    }

                    if (updatedTask.getProject() != null && updatedTask.getProject().getId() != null) {
                        projectRepository.findById(updatedTask.getProject().getId()).ifPresent(task::setProject);
                    }

                    boolean wasNotCompleted = !Boolean.TRUE.equals(task.getCompleted());
                    task.setCompleted(updatedTask.getCompleted());

                    if (Boolean.TRUE.equals(updatedTask.getCompleted()) && wasNotCompleted) {
                        tasksCompleted.increment();
                        logger.info("Task with ID {} marked as completed", id);
                    }

                    Task saved = taskRepository.save(task);
                    logger.debug("Task updated: {}", saved);
                    return saved;
                })
                .orElseThrow(() -> {
                    logger.error("Task not found with ID: {}", id);
                    return new RuntimeException("Task not found with id " + id);
                });
    }

    @Cacheable("tasks")
    public Task getTaskById(Long id) {
        logger.info("Fetching task by ID (cached): {}", id);
        return taskRepository.findById(id).orElse(null);
    }
}
