package com.example.managerapp.service;

import com.example.managerapp.model.Project;
import com.example.managerapp.repository.ProjectRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;
    private final MeterRegistry meterRegistry;
    private final AtomicInteger activeProjects = new AtomicInteger(0);

    public ProjectService(ProjectRepository projectRepository, MeterRegistry meterRegistry) {
        this.projectRepository = projectRepository;
        this.meterRegistry = meterRegistry;

        meterRegistry.gauge("app.projects.active", activeProjects);
        logger.info("ProjectService initialized with active project gauge.");
    }

    public List<Project> getAllProjects() {
        logger.info("Fetching all projects");
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        logger.info("Fetching project by ID: {}", id);
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        logger.info("Creating project: {}", project);
        activeProjects.incrementAndGet();
        Project saved = projectRepository.save(project);
        logger.debug("Project saved: {}", saved);
        return saved;
    }

    public void deleteProject(Long id) {
        logger.info("Deleting project with ID: {}", id);
        activeProjects.decrementAndGet();
        projectRepository.deleteById(id);
        logger.debug("Project with ID {} deleted", id);
    }

    public Project updateProject(Long id, Project updatedProject) {
        logger.info("Updating project with ID: {}", id);
        return projectRepository.findById(id)
                .map(project -> {
                    project.setName(updatedProject.getName());
                    project.setCreatedAt(updatedProject.getCreatedAt());
                    project.setOwner(updatedProject.getOwner());
                    Project updated = projectRepository.save(project);
                    logger.debug("Project updated: {}", updated);
                    return updated;
                })
                .orElseThrow(() -> {
                    logger.error("Project not found with ID: {}", id);
                    return new RuntimeException("Project not found with id " + id);
                });
    }
}
