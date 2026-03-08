package com.sample_project.migration_project.service;

import com.sample_project.migration_project.model.Migrant;
import com.sample_project.migration_project.repository.MigrantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class MigrantService {

    @Autowired
    private MigrantRepository repository;

    // Register a new migrant
    public Migrant registerMigrant(Migrant migrant) {
        if (repository.findByGovernmentId(migrant.getGovernmentId()).isPresent()) {
            throw new RuntimeException("Migrant with this ID already exists!");
        }
        return repository.save(migrant);
    }

    // Get all migrants
    public List<Migrant> getAllMigrants() {
        return repository.findAll();
    }
    // Add this inside MigrantService class
    public List<Migrant> searchByDestination(String state) {
        return repository.findByDestinationState(state);
    }

    // 1. UPDATE: Update an existing migrant's details
    public Migrant updateMigrant(Long id, Migrant newDetails) {
        return repository.findById(id)
                .map(migrant -> {
                    migrant.setFullName(newDetails.getFullName());
                    migrant.setOriginState(newDetails.getOriginState());
                    migrant.setDestinationState(newDetails.getDestinationState());
                    migrant.setStatus(newDetails.getStatus());

                    // Add the new fields here:
                    migrant.setReasonForMigration(newDetails.getReasonForMigration());
                    migrant.setCurrentLocation(newDetails.getCurrentLocation());
                    migrant.setDownloadCardUrl(newDetails.getDownloadCardUrl());

                    return repository.save(migrant);
                })
                .orElseThrow(() -> new RuntimeException("Migrant not found with id " + id));
    }

    // 2. DELETE: Remove a migrant from the database
    public void deleteMigrant(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Migrant not found with id " + id);
        }
        repository.deleteById(id);
    }

    public Migrant updateDocumentPath(Long id, String documentPath) {
        return repository.findById(id)
                .map(migrant -> {
                    migrant.setDocumentPath(documentPath);
                    return repository.save(migrant);
                })
                .orElseThrow(() -> new RuntimeException("Migrant not found with id " + id));
    }

    public Page<Migrant> getAllMigrantsPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repository.findAll(pageable);
    }

    public void exportToPdf(HttpServletResponse response) throws IOException {
        List<Migrant> migrants = repository.findAll();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Add a title
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Migration Tracking Report", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        // Create a table with 4 columns
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(15);

        // Table Headers
        table.addCell("ID");
        table.addCell("Full Name");
        table.addCell("Origin State");
        table.addCell("Destination State");

        // Populate Table Data
        for (Migrant migrant : migrants) {
            table.addCell(String.valueOf(migrant.getId()));
            table.addCell(migrant.getFullName());
            table.addCell(migrant.getOriginState());
            table.addCell(migrant.getDestinationState());
        }

        document.add(table);
        document.close();
    }



}