package com.sample_project.migration_project.service;

import com.sample_project.migration_project.model.Migrant;
import com.sample_project.migration_project.repository.MigrantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MigrantServiceTest {

    @Mock
    private MigrantRepository repository;

    @InjectMocks
    private MigrantService migrantService;

    private Migrant testMigrant;

    @BeforeEach
    void setUp() {
        testMigrant = new Migrant();
        testMigrant.setId(1L);
        testMigrant.setFullName("John Doe");
        testMigrant.setGovernmentId("ABC1234567");
        testMigrant.setOriginState("Bihar");
        testMigrant.setDestinationState("Maharashtra");
    }

    @Test
    void registerMigrant_Success() {
        when(repository.findByGovernmentId(testMigrant.getGovernmentId())).thenReturn(Optional.empty());
        when(repository.save(any(Migrant.class))).thenReturn(testMigrant);

        Migrant savedMigrant = migrantService.registerMigrant(testMigrant);

        assertNotNull(savedMigrant);
        assertEquals("John Doe", savedMigrant.getFullName());
        verify(repository, times(1)).save(testMigrant);
    }

    @Test
    void registerMigrant_ThrowsExceptionWhenIdExists() {
        when(repository.findByGovernmentId(testMigrant.getGovernmentId())).thenReturn(Optional.of(testMigrant));

        assertThrows(RuntimeException.class, () -> {
            migrantService.registerMigrant(testMigrant);
        });

        verify(repository, never()).save(any(Migrant.class));
    }
}