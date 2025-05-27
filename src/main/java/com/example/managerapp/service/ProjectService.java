package com.example.managerapp.service;

import com.example.managerapp.model.Project;
import com.example.managerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MeterRegistry meterRegistry;
    private final AtomicInteger activeProjects = new AtomicInteger(0);

    public ProjectService(ProjectRepository projectRepository, MeterRegistry meterRegistry) {
        this.projectRepository = projectRepository;
        this.meterRegistry = meterRegistry;

        meterRegistry.gauge("app.projects.active", activeProjects);
    }


    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        activeProjects.incrementAndGet();
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        activeProjects.decrementAndGet();
        projectRepository.deleteById(id);
    }


    public Project updateProject(Long id, Project updatedProject) {
        return projectRepository.findById(id)
                .map(project -> {
                    project.setName(updatedProject.getName());
                    project.setCreatedAt(updatedProject.getCreatedAt());
                    project.setOwner(updatedProject.getOwner());
                    return projectRepository.save(project);
                })
                .orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }
}