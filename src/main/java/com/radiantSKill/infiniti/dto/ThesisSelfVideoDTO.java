package com.radiantSKill.infiniti.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ThesisSelfVideoDTO {

    private  String  file_url;
    private  String status;
    private LocalDateTime uploaded_at;

}
