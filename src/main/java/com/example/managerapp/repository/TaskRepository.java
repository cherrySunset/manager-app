package com.example.managerapp.repository;

import com.example.managerapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByDeadlineId(Long deadlineId);
}
