package com.example.managerapp;
import com.example.managerapp.model.Notification;
import com.example.managerapp.repository.NotificationRepository;
import com.example.managerapp.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class NotificationServiceTest {
    private NotificationRepository notificationRepository;
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationRepository = mock(NotificationRepository.class);
        notificationService = new NotificationService(notificationRepository);
    }

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
