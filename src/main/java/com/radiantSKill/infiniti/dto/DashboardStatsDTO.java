package com.radiantSKill.infiniti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStatsDTO {

    private Long totalStudents;
    private Long schools;
    private Long prototypes;
    private Long investible;

}