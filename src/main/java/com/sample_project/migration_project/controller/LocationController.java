package com.sample_project.migration_project.controller;

import com.sample_project.migration_project.model.Migrant;
import com.sample_project.migration_project.repository.MigrantRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Controller
public class LocationController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MigrantRepository migrantRepository;

    public LocationController(SimpMessagingTemplate messagingTemplate, MigrantRepository migrantRepository) {
        this.messagingTemplate = messagingTemplate;
        this.migrantRepository = migrantRepository;
    }

    // When frontend sends JSON to "/app/location.update", this method catches it
    @MessageMapping("/location.update")
    @Transactional
    public void updateLocation(@Payload LocationUpdateRequest request) {

        // 1. Find the migrant in the database
        Migrant migrant = migrantRepository.findById(request.getMigrantId())
                .orElseThrow(() -> new RuntimeException("Migrant not found with ID: " + request.getMigrantId()));

        // 2. Update their GPS coordinates
        migrant.setCurrentLatitude(request.getLatitude());
        migrant.setCurrentLongitude(request.getLongitude());
        migrant.setLastLocationUpdate(LocalDateTime.now());
        migrantRepository.save(migrant);

        // 3. BROADCAST TO THE MAP
        // Anyone (like the Institution) watching this URL will instantly get the new coordinates
        String destination = "/topic/migrant/" + migrant.getId() + "/location";
        messagingTemplate.convertAndSend(destination, request);
    }

    // A simple DTO (Data Transfer Object) to grab the incoming JSON data
    public static class LocationUpdateRequest {
        private Long migrantId;
        private Double latitude;
        private Double longitude;

        public Long getMigrantId() { return migrantId; }
        public void setMigrantId(Long migrantId) { this.migrantId = migrantId; }

        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }

        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }
}
