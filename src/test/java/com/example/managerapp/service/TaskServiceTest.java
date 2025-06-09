package com.example.managerapp.service;

import com.example.managerapp.model.Task;
import com.example.managerapp.repository.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private DeadlineRepository deadlineRepository;
    private ProjectRepository projectRepository;
    private MeterRegistry meterRegistry;
    private Counter mockCounter;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        userRepository = mock(UserRepository.class);
        deadlineRepository = mock(DeadlineRepository.class);
        projectRepository = mock(ProjectRepository.class);
        meterRegistry = mock(MeterRegistry.class);
        mockCounter = mock(Counter.class);

        when(meterRegistry.counter(anyString())).thenReturn(mockCounter);

        taskService = new TaskService(
                taskRepository,
                userRepository,
                deadlineRepository,
                projectRepository,
                meterRegistry
        );
    }

    @Test
    void shouldCreateTask() {
        Task task = new Task();
        task.setTitle("Test task");

        when(taskRepository.save(task)).thenReturn(task);

        Task created = taskService.createTask(task);

        assertNotNull(created);
        assertEquals("Test task", created.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void shouldGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Sample Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task found = taskService.getTaskById(1L);

        assertNotNull(found);
        assertEquals("Sample Task", found.getTitle());
    }
}
