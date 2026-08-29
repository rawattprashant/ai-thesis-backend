package com.radiantSKill.infiniti.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private static final String BASE_DIR = "uploads";

    @Override
    public String store(MultipartFile file, String folder) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path dir = Paths.get(BASE_DIR, folder);
            Files.createDirectories(dir);
            Path path = dir.resolve(filename);
            Files.write(path, file.getBytes());
            return path.toString();
        } catch (Exception e) {
            throw new RuntimeException("File upload failed");
        }
    }
}
