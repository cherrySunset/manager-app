package com.example.managerapp.service;

import com.example.managerapp.model.Deadline;
import com.example.managerapp.model.Notification;
import com.example.managerapp.model.Task;
import com.example.managerapp.model.User;
import com.example.managerapp.repository.DeadlineRepository;
import com.example.managerapp.repository.NotificationRepository;
import com.example.managerapp.repository.TaskRepository;
import com.example.managerapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private DeadlineRepository deadlineRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Scheduled(fixedRate = 60000)
    public void checkDeadlinesAndNotify() {
        logger.info("Checking deadlines to send notifications...");
        List<Deadline> deadlines = deadlineRepository.findByNotifiedFalse();
        LocalDateTime now = LocalDateTime.now();

        for (Deadline deadline : deadlines) {
            if (deadline.getDueDate().isBefore(now.plusMinutes(5))) {
                logger.debug("Upcoming deadline found: {}", deadline);
                User user = findUserForDeadline(deadline);
                if (user == null) {
                    logger.warn("No user found for deadline ID {}", deadline.getId());
                    continue;
                }

                Notification notification = new Notification();
                notification.setUser(user);
                notification.setMessage("Deadline is coming!");
                notification.setTimestamp(LocalDateTime.now());
                notification.setStatus("UNREAD");

                notificationRepository.save(notification);
                logger.info("Notification sent to user {} for deadline {}", user.getId(), deadline.getId());

                deadline.setNotified(true);
                deadlineRepository.save(deadline);
                logger.debug("Marked deadline as notified: {}", deadline.getId());
            }
        }
    }

    private User findUserForDeadline(Deadline deadline) {
        Optional<Task> taskOpt = taskRepository.findByDeadlineId(deadline.getId());
        User user = taskOpt.map(Task::getAssignedTo).orElse(null);
        if (user != null) {
            logger.debug("User {} found for deadline {}", user.getId(), deadline.getId());
        }
        return user;
    }

    public List<Notification> getAllNotifications() {
        logger.info("Fetching all notifications");
        return notificationRepository.findAll();
    }

    public Optional<Notification> getNotificationById(Long id) {
        logger.info("Fetching notification by ID: {}", id);
        return notificationRepository.findById(id);
    }

    public Notification createNotification(Notification notification) {
        logger.info("Creating new notification: {}", notification);
        return notificationRepository.save(notification);
    }

    public Notification updateNotification(Long id, Notification updatedNotification) {
        logger.info("Updating notification with ID: {}", id);
        return notificationRepository.findById(id)
                .map(existing -> {
                    existing.setMessage(updatedNotification.getMessage());
                    existing.setStatus(updatedNotification.getStatus());
                    existing.setTimestamp(updatedNotification.getTimestamp());
                    existing.setUser(updatedNotification.getUser());
                    logger.debug("Updated notification data: {}", existing);
                    return notificationRepository.save(existing);
                })
                .orElseThrow(() -> {
                    logger.error("Notification not found with ID: {}", id);
                    return new RuntimeException("Notification not found with id " + id);
                });
    }

    public void deleteNotification(Long id) {
        logger.info("Deleting notification with ID: {}", id);
        notificationRepository.deleteById(id);
        logger.debug("Deleted notification with ID: {}", id);
    }
}
