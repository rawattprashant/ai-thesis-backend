package com.radiantSKill.infiniti.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThesisDigitalPrototypeDTO {
    private String description;
    private String fileUrl;
    private String status;
    private LocalDateTime uploadedAt;
}
