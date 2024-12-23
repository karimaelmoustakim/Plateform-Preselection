package ma.emsi.rh.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplicationDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("candidateId")
    private Long candidateId;

    @JsonProperty("positionApplied")
    private String positionApplied;

    @JsonProperty("cvUrl")
    private String cvUrl;

    @JsonProperty("coverLetter")
    private String coverLetter;

    @JsonProperty("experience")
    private int experience;

    @JsonProperty("education_level")
    private String education_level;

    @JsonProperty("relevent_experience")
    private boolean relevent_experience;

    @JsonProperty("status")
    private String status;

    @JsonProperty("prediction")
    private String prediction;

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }
    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getPositionApplied() {
        return positionApplied;
    }

    public void setPositionApplied(String positionApplied) {
        this.positionApplied = positionApplied;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getEducationLevel() {
        return education_level;
    }

    public void setEducationLevel(String educationLevel) {
        this.education_level = educationLevel;
    }

    public boolean isReleventExperience() {
        return relevent_experience;
    }

    public void setReleventExperience(boolean releventExperience) {
        this.relevent_experience = releventExperience;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ApplicationDTO{" +
                "id=" + id +
                ", candidateId=" + candidateId +
                ", positionApplied='" + positionApplied + '\'' +
                ", cvUrl='" + cvUrl + '\'' +
                ", coverLetter='" + coverLetter + '\'' +
                ", experience=" + experience +
                ", educationLevel='" + education_level + '\'' +
                ", releventExperience=" + relevent_experience +
                ", status='" + status + '\'' +
                ", prediction='" + prediction + '\'' +
                '}';
    }


}
