// DTO
package com.radiantSKill.infiniti.dto;

import lombok.Data;

@Data
public class AdminFilterRequest {
    private String status;
    private String topic;
    private String grade;
    private String schoolName;
    private int page = 0;
    private int size = 10;
}