package com.radiantSKill.infiniti.services;

import com.radiantSKill.infiniti.dto.PrincipalCommentRequestDTO;
import com.radiantSKill.infiniti.dto.PrincipalCommentResponseDTO;

public interface PrincipalCommentService {

    void saveOrUpdateComment(
            String email,
            PrincipalCommentRequestDTO request);

    PrincipalCommentResponseDTO getCommentByStudentId(
            Long studentId);
}