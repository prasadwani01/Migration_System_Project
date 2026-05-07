package com.sample_project.migration_project.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR = "uploads/";

    // 1. Whitelist the exact file types you want to allow
    private final List<String> ALLOWED_EXTENSIONS = Arrays.asList("pdf", "jpg", "jpeg", "png");
    private final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("application/pdf", "image/jpeg", "image/png");

    public String saveFile(MultipartFile file) throws IOException {
        // 2. Reject empty files immediately
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload an empty file.");
        }

        // 3. Clean the path to prevent directory traversal (e.g., blocking "../../malicious.sh")
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFilename.contains("..")) {
            throw new SecurityException("Filename contains invalid path sequences: " + originalFilename);
        }

        // 4. Validate the file type (checking both extension and MIME type)
        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        String contentType = file.getContentType();

        if (!ALLOWED_EXTENSIONS.contains(fileExtension) || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new SecurityException("Invalid file type. Only PDF, JPG, and PNG files are allowed.");
        }

        // 5. Create directory safely
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 6. Generate UUID filename and save using Files.copy (better memory management than getBytes)
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    // Helper method to safely extract the file extension
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}