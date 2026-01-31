package com.radiantSKill.infiniti.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ThesisRegistrationRequest {
    private String schoolName;
    private String gender;
    private LocalDate dateOfBirth;
    private String grade;
    private String section;
    private String studentMobile;
    private String parentEmail;
    private String parentMobile;
    private String thesisTopic;
    private String thesisIntent;
    private Boolean hasDigitalPrototype;
    private Boolean hasInvestorInterest;
}
