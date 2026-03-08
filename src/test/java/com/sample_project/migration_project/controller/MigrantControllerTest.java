package com.sample_project.migration_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample_project.migration_project.model.Migrant;
import com.sample_project.migration_project.service.MigrantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Exclude Security auto-configuration so it doesn't try to look for JWT filters
@WebMvcTest(controllers = MigrantController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
public class MigrantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MigrantService migrantService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void registerMigrant_Success() throws Exception {
        when(migrantService.registerMigrant(any(Migrant.class))).thenReturn(testMigrant);

        mockMvc.perform(post("/api/v1/migrants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMigrant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.governmentId").value("ABC1234567"));
    }
}