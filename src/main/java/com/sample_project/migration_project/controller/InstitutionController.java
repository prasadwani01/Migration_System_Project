package com.sample_project.migration_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/institution")
public class InstitutionController {

    // 1. A simple test endpoint to verify the Institution token works
    @GetMapping("/dashboard")
    public ResponseEntity<String> getInstitutionDashboard() {
        return ResponseEntity.ok("Welcome to the Institution Dashboard. Your token is valid!");
    }

    // 2. Placeholder: Get migrants specifically assigned to this institution
    @GetMapping("/migrants")
    public ResponseEntity<String> getInstitutionMigrants() {
        // Later, we will call a service method here like:
        // return ResponseEntity.ok(migrantService.getMigrantsByInstitution(institutionId));
        return ResponseEntity.ok("This will return a list of migrants for this specific institution.");
    }

    // 3. Placeholder: Update institution profile details
    // @PutMapping("/profile")
    // public ResponseEntity<InstitutionProfile> updateProfile(...) { ... }
}
