package com.example.managerapp.controller;

import com.example.managerapp.model.Deadline;
import com.example.managerapp.service.DeadlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deadlines")
public class DeadlineController {

    private static final Logger logger = LoggerFactory.getLogger(DeadlineController.class);

    @Autowired
    private DeadlineService deadlineService;

    @GetMapping
    public List<Deadline> getAllDeadlines() {
        logger.info("GET /deadlines - Fetching all deadlines");
        return deadlineService.getAllDeadlines();
    }

    @GetMapping("/{id}")
    public Deadline getDeadlineById(@PathVariable Long id) {
        logger.info("GET /deadlines/{} - Fetching deadline by ID", id);
        return deadlineService.getDeadlineById(id)
                .orElseThrow(() -> {
                    logger.error("Deadline with ID {} not found", id);
                    return new RuntimeException("Deadline not found with id " + id);
                });
    }

    @PostMapping
    public Deadline createDeadline(@RequestBody Deadline deadline) {
        logger.info("POST /deadlines - Creating new deadline: {}", deadline);
        return deadlineService.createDeadline(deadline);
    }

    @PutMapping("/{id}")
    public Deadline updateDeadline(@PathVariable Long id, @RequestBody Deadline updatedDeadline) {
        logger.info("PUT /deadlines/{} - Updating deadline", id);
        return deadlineService.updateDeadline(id, updatedDeadline);
    }

    @DeleteMapping("/{id}")
    public void deleteDeadline(@PathVariable Long id) {
        logger.info("DELETE /deadlines/{} - Deleting deadline", id);
        deadlineService.deleteDeadline(id);
    }
}
