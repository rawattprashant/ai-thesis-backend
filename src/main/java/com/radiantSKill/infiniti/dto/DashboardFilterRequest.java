package com.radiantSKill.infiniti.dto;

import lombok.Data;

@Data
public class DashboardFilterRequest {

    private String gender;

    private String grade;

    private String section;

    private Boolean digitalPrototype;

    private Boolean investment;

}