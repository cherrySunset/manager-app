package com.example.managerapp.service;
import com.example.managerapp.model.Notification;
import com.example.managerapp.repository.DeadlineRepository;
import com.example.managerapp.repository.NotificationRepository;
import com.example.managerapp.repository.TaskRepository;
import com.example.managerapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private DeadlineRepository deadlineRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldCreateNotification() {
        Notification notification = new Notification();
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification created = notificationService.createNotification(notification);
        assertNotNull(created);
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void shouldGetAllNotifications() {
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());
        when(notificationRepository.findAll()).thenReturn(notifications);

        List<Notification> result = notificationService.getAllNotifications();
        assertEquals(2, result.size());
        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteNotification() {
        Long id = 1L;
        doNothing().when(notificationRepository).deleteById(id);

        notificationService.deleteNotification(id);
        verify(notificationRepository, times(1)).deleteById(id);
    }
}