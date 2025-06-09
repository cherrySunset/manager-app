package com.example.managerapp.service;

import com.example.managerapp.model.Project;
import com.example.managerapp.model.User;
import com.example.managerapp.repository.ProjectRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;
    private MeterRegistry meterRegistry;
    private Counter mockCounter;


    @BeforeEach
    public void setUp() {
        projectRepository = mock(ProjectRepository.class);
        meterRegistry = mock(MeterRegistry.class);
        mockCounter = mock(Counter.class);

        when(meterRegistry.counter(anyString(), any(String[].class))).thenReturn(mockCounter);

        projectService = new ProjectService(projectRepository, meterRegistry);
    }

    @Test
    public void testGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(List.of(new Project()));
        List<Project> result = projectService.getAllProjects();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetProjectById() {
        Project project = new Project(1L, "Demo", LocalDateTime.now(), new User(), null);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.getProjectById(1L);
        assertTrue(result.isPresent());
        assertEquals("Demo", result.get().getName());
    }

    @Test
    public void testCreateProject() {
        Project project = new Project(null, "New Project", LocalDateTime.now(), new User(), null);
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.createProject(project);
        assertEquals("New Project", result.getName());
    }

    @Test
    public void testDeleteProject() {
        doNothing().when(projectRepository).deleteById(1L);
        projectService.deleteProject(1L);
        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateProject() {
        Project existing = new Project(1L, "Old", LocalDateTime.now(), new User(), null);
        Project updated = new Project(1L, "Updated", LocalDateTime.now(), new User(), null);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(existing)).thenReturn(existing);

        Project result = projectService.updateProject(1L, updated);
        assertEquals("Updated", result.getName());
    }
}
