package com.radiantSKill.infiniti.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ThesisRegistrationResponseDTO {

    private String schoolName;
    private String gender;
    private LocalDate dateOfBirth;
    private String grade;
    private String section;

    private String studentEmail;
    private String studentMobile;

    private String parentEmail;
    private String parentMobile;

    private String thesisTopic;
    private String thesisIntent;

    private Boolean hasDigitalPrototype;
    private Boolean hasInvestorInterest;

    private String status;
}