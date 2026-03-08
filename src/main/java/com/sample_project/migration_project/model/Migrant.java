package com.sample_project.migration_project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*; // Import validation rules
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "migrants", indexes = {
        @Index(name = "idx_destination_state", columnList = "destinationState")
})
public class Migrant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String documentPath;
    private String reasonForMigration;
    private String currentLocation;
    private String downloadCardUrl;

    @Column(nullable = false)
    @NotBlank(message = "Full Name cannot be empty") // Rule 1
    @Size(min = 3, message = "Name must be at least 3 characters") // Rule 2
    private String fullName;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Government ID is required")
    @Pattern(regexp = "^[A-Z0-9]{10,12}$", message = "ID must be 10-12 alphanumeric characters") // Rule 3
    private String governmentId; // Aadhaar/PAN

    @NotBlank(message = "Origin State is required")
    private String originState;

    @NotBlank(message = "Destination State is required")
    private String destinationState;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Migration date cannot be in the future") // Rule 4
    private LocalDate migrationDate;

    @Enumerated(EnumType.STRING)
    private MigrationStatus status; // e.g., "Pending", "Approved"
}