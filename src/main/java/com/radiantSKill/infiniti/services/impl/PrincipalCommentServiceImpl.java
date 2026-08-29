package com.radiantSKill.infiniti.services.impl;

import com.radiantSKill.infiniti.dto.PrincipalCommentRequestDTO;
import com.radiantSKill.infiniti.dto.PrincipalCommentResponseDTO;
import com.radiantSKill.infiniti.entity.*;
import com.radiantSKill.infiniti.repository.*;
import com.radiantSKill.infiniti.services.PrincipalCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrincipalCommentServiceImpl implements PrincipalCommentService {

    private final AppUserRepository appUserRepository;
    private final PrincipalRepository principalRepository;
    private final ThesisRegistrationRepository thesisRegistrationRepository;
    private final PrincipalStudentCommentRepository commentRepository;
    private final PrincipalStudentCommentHistoryRepository historyRepository;

    @Override
    @Transactional
    public void saveOrUpdateComment(
            String email,
            PrincipalCommentRequestDTO request) {

        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        SchoolPrincipal principal = principalRepository
                .findByAppUser_Id(appUser.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Principal not found"));

        ThesisRegistration student =
                thesisRegistrationRepository.findById(
                                request.getStudentId())
                        .orElseThrow(() ->
                                new IllegalArgumentException("Student not found"));

        PrincipalStudentComment comment =
                commentRepository
                        .findByThesisRegistration_Id(request.getStudentId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Comment not found"));

        // First Comment
        if (comment == null) {

            comment = new PrincipalStudentComment();

            comment.setThesisRegistration(student);
            comment.setPrincipal(principal);
            comment.setCommentText(request.getComment());

            comment.setCreatedBy(appUser);
            comment.setUpdatedBy(appUser);

            commentRepository.save(comment);

            return;
        }

        // Save old comment into history
        PrincipalStudentCommentHistory history =
                new PrincipalStudentCommentHistory();

        history.setComment(comment);
        history.setOldComment(comment.getCommentText());
        history.setModifiedBy(appUser);

        historyRepository.save(history);

        // Update current comment
        comment.setCommentText(request.getComment());
        comment.setUpdatedBy(appUser);

        commentRepository.save(comment);
    }

    @Override
    public PrincipalCommentResponseDTO getCommentByStudentId(
            Long studentId) {

        PrincipalStudentComment comment =
                commentRepository
                        .findByThesisRegistration_Id(studentId)
                        .orElseThrow(() ->
                                new RuntimeException("Comment not found"));

        return mapToResponse(comment);
    }

    private PrincipalCommentResponseDTO mapToResponse(
            PrincipalStudentComment comment) {

        PrincipalCommentResponseDTO dto =
                new PrincipalCommentResponseDTO();

        dto.setId(comment.getId());

        dto.setStudentId(
                comment.getThesisRegistration().getId());

        AppUser studentUser =
                comment.getThesisRegistration().getStudent();

        dto.setStudentName(
                studentUser.getFirstName() + " " +
                        studentUser.getLastName());

        dto.setPrincipalId(
                comment.getPrincipal().getId());

        dto.setPrincipalName(
                comment.getPrincipal().getPrincipalName());

        dto.setSchoolName(
                comment.getPrincipal().getSchoolName());

        dto.setComment(
                comment.getCommentText());

        dto.setUpdatedAt(
                comment.getUpdatedAt());

        return dto;
    }
}