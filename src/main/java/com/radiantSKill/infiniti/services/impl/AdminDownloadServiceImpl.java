package com.radiantSKill.infiniti.services.impl;

import com.radiantSKill.infiniti.entity.*;
import com.radiantSKill.infiniti.repository.AppUserRepository;
import com.radiantSKill.infiniti.repository.StudentSubmissionStoreRepository;
import com.radiantSKill.infiniti.services.AdminDownloadService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class AdminDownloadServiceImpl implements AdminDownloadService {

    private final AppUserRepository userRepository;
    private final StudentSubmissionStoreRepository storeRepository;

    private static final String BASE_PATH = "/home/rdsapp/backend/";

    @Override
    public void downloadStudentZip(Long studentId, HttpServletResponse response) throws IOException {

        AppUser student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentSubmissionStore store = storeRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        String baseFileName = buildBaseFileName(student);

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + baseFileName + ".zip");

        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());

        // ✅ FILES FROM STORAGE
        addFile(zipOut, getFileUrl(store.getDigitalPrototype()));
        addFile(zipOut, getFileUrl(store.getFinancialModel()));
        addFile(zipOut, getFileUrl(store.getThesisPresentation()));
        addFile(zipOut, getFileUrl(store.getSelfieVideo()));

        // ✅ POC TEXT FILE
        if (store.getProofOfConcept() != null) {
            String content = store.getProofOfConcept().getContent();
            addTextFile(zipOut,
                    "Proof Of Concept/" + baseFileName + "_poc.txt",
                    content);
        }

        // ✅ RESEARCH TEXT FILES
        if (store.getThesisResearch() != null) {
            ThesisResearch research = store.getThesisResearch();

            addTextFile(zipOut,
                    "Research/" + baseFileName + "_research_text.txt",
                    research.getResearchText());

            addTextFile(zipOut,
                    "Research/" + baseFileName + "_thoughts_text.txt",
                    research.getThoughtsText());
        }

        zipOut.close();
    }

    private String buildBaseFileName(AppUser student) {
        return student.getFirstName() + "_"
                + student.getLastName() + "_"
                + student.getId() + "_"
                + System.currentTimeMillis();
    }

    private String getFileUrl(Object entity) {
        if (entity == null) return null;
        try {
            return (String) entity.getClass().getMethod("getFileUrl").invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }

    private void addFile(ZipOutputStream zipOut, String dbPath) {

        if (dbPath == null || dbPath.isBlank()) return;

        File file = new File(BASE_PATH + dbPath);
        if (!file.exists()) return;

        try (FileInputStream fis = new FileInputStream(file)) {

            String cleanFileName = removeUUID(file.getName());

            String relativePath = dbPath.substring("uploads/".length());

            String folderPath = "";
            int lastSlash = relativePath.lastIndexOf("/");
            if (lastSlash != -1) {
                folderPath = relativePath.substring(0, lastSlash + 1);
            }

            ZipEntry entry = new ZipEntry(folderPath + cleanFileName);
            zipOut.putNextEntry(entry);

            byte[] buffer = new byte[1024];
            int len;

            while ((len = fis.read(buffer)) > 0) {
                zipOut.write(buffer, 0, len);
            }

            zipOut.closeEntry();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ ADD TEXT FILE TO ZIP
    private void addTextFile(ZipOutputStream zipOut, String path, String content) {

        if (content == null || content.isBlank()) return;

        try {
            ZipEntry entry = new ZipEntry(path);
            zipOut.putNextEntry(entry);

            byte[] data = content.getBytes(StandardCharsets.UTF_8);
            zipOut.write(data, 0, data.length);

            zipOut.closeEntry();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String removeUUID(String filename) {
        return filename.replaceFirst("^[a-f0-9\\-]{36}_", "");
    }
}