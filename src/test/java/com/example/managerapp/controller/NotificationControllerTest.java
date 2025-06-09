package com.example.managerapp.controller;

import com.example.managerapp.model.Notification;
import com.example.managerapp.repository.DeadlineRepository;
import com.example.managerapp.repository.NotificationRepository;
import com.example.managerapp.repository.TaskRepository;
import com.example.managerapp.repository.UserRepository;
import com.example.managerapp.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@Import({NotificationControllerTest.MockConfig.class, NotificationControllerTest.SecurityBypass.class})
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationService notificationService;

    @Test
    void shouldReturnNotifications() throws Exception {
        Notification n = new Notification(1L, null, "Test", LocalDateTime.now(), "UNREAD");
        when(notificationService.getAllNotifications()).thenReturn(List.of(n));

        mockMvc.perform(get("/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public NotificationRepository notificationRepository() {
            return mock(NotificationRepository.class);
        }

        @Bean
        public DeadlineRepository deadlineRepository() {
            return mock(DeadlineRepository.class);
        }

        @Bean
        public UserRepository userRepository() {
            return mock(UserRepository.class);
        }

        @Bean
        public TaskRepository taskRepository() {
            return mock(TaskRepository.class);
        }

        @Bean
        public NotificationService notificationService() {
            return new NotificationService();
        }
    }

    @TestConfiguration
    static class SecurityBypass {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}
