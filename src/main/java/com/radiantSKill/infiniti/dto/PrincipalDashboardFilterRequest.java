package com.radiantSKill.infiniti.dto;

import lombok.Data;

@Data
public class PrincipalDashboardFilterRequest {
    private String gender;
    private String grade;
    private String section;
    private String digitalPrototype;
    private String investment;

    // ✅ NEW (required for submissions API)
    private String status;        // overallStatus
    private String topic;         // thesisTopic
    private String schoolName;
}
