package com.radiantSKill.infiniti.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThesisPresentationDTO {
    private String file_url;
    private String status;
    private String description;
    private LocalDateTime uploaded_at;
}
