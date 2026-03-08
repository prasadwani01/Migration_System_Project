package com.sample_project.migration_project.repository;

import com.sample_project.migration_project.model.Migrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MigrantRepository extends JpaRepository<Migrant, Long> {
    // Custom method to find by Gov ID
    Optional<Migrant> findByGovernmentId(String governmentId);
    // 2. Find all migrants moving to a specific state (The missing part!)
    // Spring automatically turns this into:
    // SELECT * FROM migrants WHERE destination_state = ?
    List<Migrant> findByDestinationState(String destinationState);
}