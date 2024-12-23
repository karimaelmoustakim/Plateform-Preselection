package ma.emsi.application.module;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "applications")
public class ApplicationE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidate_id", nullable = false)
    private Long candidateId; // Lien avec l'utilisateur

    @Column(name = "candidate_name")
    //@NotEmpty(message = "Le nom du candidat est obligatoire.")
    private String candidateName= "Unknown"; // Nom du candidat

    @Column(name = "position_applied", nullable = false)
    @NotEmpty(message = "Le poste souhaité est obligatoire.")
    private String positionApplied; // Poste souhaité

    @Column(nullable = false)
    @NotEmpty(message = "Le CV est obligatoire.")
    @Pattern(regexp = ".*\\.pdf$", message = "Le CV doit être un fichier PDF.")
    private String cvUrl; // URL du CV (uploadé sur un système de stockage)

    private String coverLetter; // Lettre de motivation

    @Column(name = "experience")
    @Min(value = 0, message = "L'expérience doit être positive.")
    private int Experience; // Années d'expérience

    @Column(name = "application_date")
    private LocalDate applicationDate; // Date de candidature

    // Champ unique pour le niveau d'éducation
    @Column(name = "education_level", nullable = false)
    @NotEmpty(message = "Le niveau d'éducation est obligatoire.")
    private String educationLevel; // Exemple : "Graduate", "Masters", "PhD", "Unknown"

    // Champs ajoutés pour l'expérience pertinente (variables indicatrices)
    @Column(name = "relevent_experience", nullable = false)
    private boolean relevent_experience; // true = a de l'expérience pertinente, false = non

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.EN_ATTENTE; // Par défaut : "En attente"

    public boolean isRelevent_experience() {
        return relevent_experience;
    }

    public void setRelevent_experience(boolean relevent_experience) {
        this.relevent_experience = relevent_experience;
    }
}
