package ma.emsi.displayservice.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class ApplicationDTO {
    private Long id;
    private String candidateName;
    private String positionApplied;
    private int experience;
    private String educationLevel;
    private String status;
    private String cvUrl;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate applicationDate;

    // Getters et Setters

    public Long getId() {
        return id;
    }

    // ... autres getters et setters ...

    public void setId(Long id) {
        this.id = id;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getPositionApplied() {
        return positionApplied;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public void setPositionApplied(String positionApplied) {
        this.positionApplied = positionApplied;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    @Override
    public String toString() {
        return "ApplicationDTO{" +
                "id=" + id +
                ", candidateName='" + candidateName + '\'' +
                ", positionApplied='" + positionApplied + '\'' +
                ", experience=" + experience +
                ", educationLevel='" + educationLevel + '\'' +
                ", status='" + status + '\'' +
                ", applicationDate=" + applicationDate +
                '}';
    }
}
