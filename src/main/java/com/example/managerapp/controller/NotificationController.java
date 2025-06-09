package com.example.managerapp.controller;

import com.example.managerapp.model.Notification;
import com.example.managerapp.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notification> getAllNotifications() {
        logger.info("GET /notifications - Fetching all notifications");
        return notificationService.getAllNotifications();
    }

    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable Long id) {
        logger.info("GET /notifications/{} - Fetching notification by ID", id);
        return notificationService.getNotificationById(id)
                .orElseThrow(() -> {
                    logger.error("Notification with ID {} not found", id);
                    return new RuntimeException("Notification not found with id " + id);
                });
    }

    @PostMapping
    public Notification createNotification(@RequestBody Notification notification) {
        logger.info("POST /notifications - Creating new notification: {}", notification);
        return notificationService.createNotification(notification);
    }

    @PutMapping("/{id}")
    public Notification updateNotification(@PathVariable Long id, @RequestBody Notification updatedNotification) {
        logger.info("PUT /notifications/{} - Updating notification", id);
        return notificationService.updateNotification(id, updatedNotification);
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Long id) {
        logger.info("DELETE /notifications/{} - Deleting notification", id);
        notificationService.deleteNotification(id);
    }
}
