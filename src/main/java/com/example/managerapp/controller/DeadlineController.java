package com.example.managerapp.controller;

import com.example.managerapp.model.Deadline;
import com.example.managerapp.service.DeadlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deadlines")
public class DeadlineController {
    @Autowired
    private DeadlineService deadlineService;

    @GetMapping
    public List<Deadline> getAllDeadlines() {
        return deadlineService.getAllDeadlines();
    }

    @GetMapping("/{id}")
    public Deadline getDeadlineById(@PathVariable Long id) {
        return deadlineService.getDeadlineById(id)
                .orElseThrow(() -> new RuntimeException("Deadline not found with id " + id));
    }

    @PostMapping
    public Deadline createDeadline(@RequestBody Deadline deadline) {
        return deadlineService.createDeadline(deadline);
    }

    @PutMapping("/{id}")
    public Deadline updateDeadline(@PathVariable Long id, @RequestBody Deadline updatedDeadline) {
        return deadlineService.updateDeadline(id, updatedDeadline);
    }

    @DeleteMapping("/{id}")
    public void deleteDeadline(@PathVariable Long id) {
        deadlineService.deleteDeadline(id);
    }
}
