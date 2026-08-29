package com.radiantSKill.infiniti.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardSummaryDTO {
    private long totalStudents;
    private long totalRegistrations;
    private long pocCompleted;
    private long financialModelCompleted;
    private long presentationsCompleted;
    private long finalSubmissions;
}