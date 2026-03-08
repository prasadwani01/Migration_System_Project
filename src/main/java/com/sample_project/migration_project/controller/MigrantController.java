package com.sample_project.migration_project.controller;

import com.sample_project.migration_project.model.Migrant;
import com.sample_project.migration_project.repository.MigrantRepository;
import com.sample_project.migration_project.service.FileStorageService;
import com.sample_project.migration_project.service.MigrantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.sample_project.migration_project.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;

@RestController
@RequestMapping("/api/v1/migrants")
@CrossOrigin(origins = "http://localhost:3000") // Allow React Frontend
public class MigrantController {

    @Autowired
    private MigrantService service;

    // Inject it via constructor or @Autowired:
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private MigrantRepository repository;

    @PostMapping
    public Migrant addMigrant(@Valid @RequestBody Migrant migrant) {
        return service.registerMigrant(migrant);
    }

    @GetMapping
    public List<Migrant> getAllMigrants() {
        return service.getAllMigrants();
    }
    // Add this BELOW the getAllMigrants() method

    // API to search migrants by destination state
    // Example usage: /api/v1/migrants/search?state=Delhi
    @GetMapping("/search")
    public List<Migrant> searchByState(@RequestParam String state) {
        // We need to inject the repository directly or add this method to the Service
        // For simplicity, let's add the logic to MigrantService first (see below)
        return service.searchByDestination(state);
    }

    // 4. PUT: Update a migrant (e.g., Fix a typo or change Status)
    // URL: /api/v1/migrants/{id}  (Example: /api/v1/migrants/1)
    @PutMapping("/{id}")
    public ResponseEntity<Migrant> updateMigrant(@PathVariable("id") Long id, @RequestBody Migrant migrant) {
        Migrant updatedMigrant = service.updateMigrant(id, migrant);
        return ResponseEntity.ok(updatedMigrant);
    }

    // 5. DELETE: Remove a migrant
    // URL: /api/v1/migrants/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMigrant(@PathVariable("id") Long id) {
        service.deleteMigrant(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload")
    public Migrant uploadDocument(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws Exception {
        String filePath = fileStorageService.saveFile(file);
        return service.updateDocumentPath(id, filePath);
    }

    @GetMapping("/{id}/identity/card")
    public ResponseEntity<byte[]> downloadCard(@PathVariable Long id) {
        // Fetch the migrant using your existing repository/service
        Migrant migrant = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Migrant not found with id " + id));

        byte[] pdfBytes = pdfService.generateMigrantCard(migrant);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "MigrantCard_" + migrant.getGovernmentId() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Migrant>> getMigrantsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return ResponseEntity.ok(service.getAllMigrantsPaginated(page, size, sortBy));
    }
    @GetMapping("/export/report/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        // Set the response type to PDF
        response.setContentType("application/pdf");

        // Generate a filename with the current date/time
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=migrants_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        // Call the service to generate and write the PDF
        service.exportToPdf(response);
    }

}