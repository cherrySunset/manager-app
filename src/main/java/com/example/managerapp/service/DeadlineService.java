package com.example.managerapp.service;

import com.example.managerapp.model.Deadline;
import com.example.managerapp.repository.DeadlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeadlineService {
    private final DeadlineRepository deadlineRepository;

    @Autowired
    public DeadlineService(DeadlineRepository deadlineRepository) {
        this.deadlineRepository = deadlineRepository;
    }

    public Deadline createDeadline(Deadline deadline) {
        return deadlineRepository.save(deadline);
    }

    public List<Deadline> getAllDeadlines() {
        return deadlineRepository.findAll();
    }

    public Optional<Deadline> getDeadlineById(Long id) {
        return deadlineRepository.findById(id);
    }

    public Deadline updateDeadline(Long id, Deadline updatedDeadline) {
        return deadlineRepository.findById(id)
                .map(deadline -> {
                    deadline.setDueDate(updatedDeadline.getDueDate());
                    deadline.setNotified(updatedDeadline.getNotified());
                    return deadlineRepository.save(deadline);
                })
                .orElseThrow(() -> new RuntimeException("Deadline not found"));
    }

    public void deleteDeadline(Long id) {
        deadlineRepository.deleteById(id);
    }
}
