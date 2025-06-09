package com.example.managerapp.controller;

import com.example.managerapp.model.Deadline;
import com.example.managerapp.service.DeadlineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeadlineController.class)
@Import({DeadlineControllerTest.MockConfig.class, DeadlineControllerTest.TestSecurityConfig.class})
class DeadlineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeadlineService deadlineService;

    @Test
    void shouldGetAllDeadlines() throws Exception {
        Deadline d = new Deadline(1L, LocalDateTime.now().plusDays(1), false);
        when(deadlineService.getAllDeadlines()).thenReturn(List.of(d));

        mockMvc.perform(get("/deadlines")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public DeadlineService deadlineService() {
            return mock(DeadlineService.class);
        }
    }

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}
