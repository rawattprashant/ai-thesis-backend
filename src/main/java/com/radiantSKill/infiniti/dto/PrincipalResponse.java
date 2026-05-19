package com.radiantSKill.infiniti.dto;

public class PrincipalResponse {
        private Long id;
        private String principalName;
        private String schoolName;
        private String schoolAddress;
        private String principalEmail;

        // Getters and Setters

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getPrincipalName() {
            return principalName;
        }

        public void setPrincipalName(String principalName) {
            this.principalName = principalName;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getSchoolAddress() {
            return schoolAddress;
        }

        public void setSchoolAddress(String schoolAddress) {
            this.schoolAddress = schoolAddress;
        }

        public String getPrincipalEmail() {
            return principalEmail;
        }

        public void setPrincipalEmail(String principalEmail) {
            this.principalEmail = principalEmail;
        }
    }

