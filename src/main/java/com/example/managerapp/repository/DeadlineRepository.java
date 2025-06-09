package com.example.managerapp.repository;

import com.example.managerapp.model.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeadlineRepository extends JpaRepository<Deadline, Long> {
    List<Deadline> findByNotifiedFalse();
}
