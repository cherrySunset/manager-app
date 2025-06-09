package com.example.managerapp.service;
import com.example.managerapp.model.Deadline;
import com.example.managerapp.repository.DeadlineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeadlineServiceTest {
    private DeadlineRepository deadlineRepository;
    private DeadlineService deadlineService;

    @BeforeEach
    void setUp() {
        deadlineRepository = mock(DeadlineRepository.class);
        deadlineService = new DeadlineService(deadlineRepository);
    }

    @Test
    void shouldCreateDeadline() {
        Deadline deadline = new Deadline();
        when(deadlineRepository.save(deadline)).thenReturn(deadline);

        Deadline created = deadlineService.createDeadline(deadline);
        assertNotNull(created);
        verify(deadlineRepository, times(1)).save(deadline);
    }

    @Test
    void shouldUpdateDeadline() {
        Long id = 1L;
        Deadline existing = new Deadline();
        existing.setId(id);
        existing.setDueDate(LocalDateTime.now());
        existing.setNotified(false);

        Deadline updated = new Deadline();
        updated.setNotified(true);

        when(deadlineRepository.findById(id)).thenReturn(Optional.of(existing));
        when(deadlineRepository.save(any())).thenReturn(existing);

        Deadline result = deadlineService.updateDeadline(id, updated);
        assertTrue(result.getNotified());
        verify(deadlineRepository, times(1)).save(existing);
    }

    @Test
    void shouldThrowIfDeadlineNotFound() {
        Long id = 999L;
        when(deadlineRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> deadlineService.updateDeadline(id, new Deadline()));
        verify(deadlineRepository, never()).save(any());
    }
}
