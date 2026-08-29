package com.radiantSKill.infiniti.services.impl;

import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.StudentSubmissionStore;
import com.radiantSKill.infiniti.entity.ThesisRegistration;
import com.radiantSKill.infiniti.entity.ThesisResearch;
import com.radiantSKill.infiniti.repository.AppUserRepository;
import com.radiantSKill.infiniti.repository.PrincipalRepository;
import com.radiantSKill.infiniti.repository.StudentSubmissionStoreRepository;
import com.radiantSKill.infiniti.repository.ThesisRegistrationRepository;
import com.radiantSKill.infiniti.services.PrincipalDownloadService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDownloadServiceImpl implements PrincipalDownloadService {

        private final AppUserRepository userRepository;
        private final StudentSubmissionStoreRepository storeRepository;
        private final PrincipalRepository principalRepository;
        private final ThesisRegistrationRepository thesisRegistrationRepository;
        private static final String BASE_PATH = "/home/rdsapp/backend/";

        @Override
        public void downloadStudentZip(
                Long studentId,
                String principalEmail,
                HttpServletResponse response
        ) throws IOException {

            // Principal School
            PrincipalRepository principalSchool = (PrincipalRepository) principalRepository
                            .findByPrincipalEmail(principalEmail)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Principal school not found"
                                    ));

            // Student
            AppUser student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

            // Registration
            ThesisRegistration registration = thesisRegistrationRepository.findByStudent(student).orElseThrow(() -> new RuntimeException("Registration not found"));

            StudentSubmissionStore store = storeRepository.findByStudent(student).orElseThrow(() -> new RuntimeException("Submission not found"));
            String zipName = buildZipName(student);
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
            log.info("Starting ZIP download for studentId={}", studentId);
            try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
                addFile(zipOut, getPresentationPath(store), "presentation/");
                addFile(zipOut, getSelfiePath(store), "selfie-video/");
                addFile(zipOut, getPrototypePath(store), "digital-prototype/");
                addFile(zipOut, getFinancialPath(store), "financial-model/");
                addPocFile(zipOut, store, student);
                addResearchFiles(zipOut, store, student);
                log.info("ZIP creation completed for studentId={}", studentId);
            }
        }

        // ========================= ZIP NAME =========================

        private String buildZipName(AppUser student) {
            return student.getFirstName() + "_"
                    + student.getLastName() + "_"
                    + student.getId() + "_"
                    + System.currentTimeMillis()
                    + ".zip";
        }

        // ========================= PATH GETTERS =========================

        private String getPresentationPath(StudentSubmissionStore store) {
            return store.getThesisPresentation() != null
                    ? store.getThesisPresentation().getFileUrl()
                    : null;
        }
        private String getSelfiePath(StudentSubmissionStore store) {
            return store.getSelfieVideo() != null
                    ? store.getSelfieVideo().getFileUrl()
                    : null;
        }

        private String getPrototypePath(StudentSubmissionStore store) {
            return store.getDigitalPrototype() != null
                    ? store.getDigitalPrototype().getFileUrl()
                    : null;
        }

        private String getFinancialPath(StudentSubmissionStore store) {
            return store.getFinancialModel() != null
                    ? store.getFinancialModel().getFileUrl()
                    : null;
        }

        // ========================= ADD FILE =========================

        private void addFile(ZipOutputStream zipOut, String dbPath, String folder) {
            if (dbPath == null || dbPath.isBlank()) return;
            try {
                File file = new File(BASE_PATH + dbPath);
                log.info(
                        "Trying file path: {}",
                        file.getAbsolutePath()
                );
                if (!file.exists()) {
                    log.warn(
                            "File not found: {}",
                            file.getAbsolutePath()
                    );
                    return;
                }
                String cleanName = file.getName().replaceFirst("^[^_]+_", "");
                ZipEntry entry = new ZipEntry(folder + cleanName);
                zipOut.putNextEntry(entry);
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = fis.read(buffer)) != -1) {
                        zipOut.write(buffer, 0, len);
                    }
                }

                zipOut.closeEntry();
                log.info("File added to ZIP: {}", cleanName);

            } catch (Exception e) {
                log.error(
                        "Error adding file to ZIP: {}",
                        dbPath,
                        e
                );
            }
        }

        // ========================= POC FILE =========================

        private void addPocFile(ZipOutputStream zipOut, StudentSubmissionStore store, AppUser student) {
            try {
                if (store.getProofOfConcept() == null)
                    return;
                String content = store.getProofOfConcept().getContent();
                if (content == null || content.isBlank())
                    return;
                String fileName = buildBaseFileName(student) + ".txt";
                ZipEntry entry = new ZipEntry("Proof Of Concept/" + fileName);
                zipOut.putNextEntry(entry);
                zipOut.write(content.getBytes(StandardCharsets.UTF_8));
                zipOut.closeEntry();
                log.info("POC file added");
            } catch (Exception e) {
                log.error("Error adding POC file", e);
            }
        }

        // ========================= RESEARCH FILES =========================

        private void addResearchFiles(ZipOutputStream zipOut, StudentSubmissionStore store, AppUser student) {
            try {
                if (store.getThesisResearch() == null)
                    return;
                ThesisResearch research = store.getThesisResearch();
                String baseName = buildBaseFileName(student);
                if (research.getResearchText() != null
                        && !research.getResearchText().isBlank()) {
                    ZipEntry entry = new ZipEntry(
                                    "Research/"
                                            + baseName
                                            + "_research_text.txt"
                            );

                    zipOut.putNextEntry(entry);
                    zipOut.write(research.getResearchText().getBytes(StandardCharsets.UTF_8));
                    zipOut.closeEntry();
                }

                if (research.getThoughtsText() != null
                        && !research.getThoughtsText().isBlank()) {
                    ZipEntry entry =
                            new ZipEntry(
                                    "Research/"
                                            + baseName
                                            + "_thought_text.txt"
                            );

                    zipOut.putNextEntry(entry);
                    zipOut.write(research.getThoughtsText().getBytes(StandardCharsets.UTF_8));
                    zipOut.closeEntry();
                }

            } catch (Exception e) {
                log.error("Error adding research files", e);
            }
        }

        // ========================= COMMON NAME =========================

        private String buildBaseFileName(AppUser student) {

            return student.getFirstName() + "_"
                    + student.getLastName() + "_"
                    + student.getId() + "_"
                    + System.currentTimeMillis();
        }
    }
