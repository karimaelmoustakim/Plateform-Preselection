package ma.emsi.application.controller;

import jakarta.servlet.http.HttpSession;
import ma.emsi.application.Repository.ApplicationRepository;
import ma.emsi.application.module.ApplicationE;
import ma.emsi.application.module.ApplicationStatus;
import ma.emsi.application.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Créer une nouvelle candidature
//    @PostMapping
//    public ResponseEntity<ApplicationE> createApplication(@RequestBody ApplicationE application) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Vérifier si l'utilisateur a le rôle CANDIDATE
//        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CANDIDATE"))) {
//            // Associer automatiquement le candidat connecté
//            String candidateEmail = authentication.getName(); // Récupère l'email de l'utilisateur connecté
//            Long candidateId = applicationService.getCandidateIdByEmail(candidateEmail); // Méthode à implémenter
//            if (candidateId == null) {
//                return ResponseEntity.badRequest().body(null); // Erreur si aucun utilisateur n'est trouvé
//            }
//
//            application.setCandidateId(candidateId);
//            ApplicationE createdApplication = applicationService.createApplication(application);
//            return ResponseEntity.ok(createdApplication);
//        } else {
//            return ResponseEntity.status(403).body(null); // Forbidden
//        }
//    }

    @PostMapping
    public ResponseEntity<ApplicationE> createApplication(@RequestBody ApplicationE application) {
        // Suppression de l'authentification pour le test
        application.setCandidateId(1L); // ID fictif ou hardcodé pour tester
        ApplicationE createdApplication = applicationService.createApplication(application);
        return ResponseEntity.ok(createdApplication);
    }


//    @GetMapping("/my-applications")
//    public ResponseEntity<?> getMyApplications() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non authentifié.");
//        }
//
//        boolean isCandidate = authentication.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CANDIDATE"));
//        boolean isRH = authentication.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_RH"));
//
//        if (isCandidate) {
//            String candidateEmail = authentication.getName();
//            Long candidateId = applicationService.getCandidateIdByEmail(candidateEmail);
//            if (candidateId == null) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Candidat introuvable.");
//            }
//
//            List<ApplicationE> applications = applicationService.getApplicationsByCandidate(candidateId);
//            return ResponseEntity.ok(applications);
//        } else if (isRH) {
//            List<ApplicationE> allApplications = applicationService.getAllApplications();
//            return ResponseEntity.ok(allApplications);
//        }
//
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès interdit.");
//    }



    @GetMapping
    public ResponseEntity<List<ApplicationE>> getAllApplications() {
        List<ApplicationE> applications = applicationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }


    // Récupérer une candidature par ID (vérification de l'accès)
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationE> getApplicationById(@PathVariable Long id) {
        ApplicationE application = applicationService.getApplicationById(id);
        return ResponseEntity.ok(application);
    }

    // Supprimer une candidature (protection par ID du candidat)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationE> updateStatus(@PathVariable Long id, @RequestBody String status) {
        ApplicationE application = applicationService.getApplicationById(id);
        application.setStatus(ApplicationStatus.valueOf(status.toUpperCase()));
//        ApplicationE updatedApplication = applicationService.createApplication(application);
        applicationService.saveApplication(application);
        return ResponseEntity.ok(application);
    }
    @GetMapping("status")
    public ResponseEntity<List<ApplicationE>> getApplicationsByStatus(@RequestParam String status) {
        // Convertir le paramètre en enum
        ApplicationStatus applicationStatus = ApplicationStatus.valueOf(status.toUpperCase());
        List<ApplicationE> applications = applicationService.getApplicationsByStatus(applicationStatus);
        return ResponseEntity.ok(applications);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationE> updateApplication(@PathVariable Long id, @RequestBody ApplicationE updatedApplication) {
        // Vérifier si l'application existe
        ApplicationE existingApplication = applicationService.getApplicationById(id);
        if (existingApplication == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found si l'application n'existe pas
        }

        // Mettre à jour les champs de l'application existante
        existingApplication.setCandidateId(updatedApplication.getCandidateId());
        existingApplication.setCandidateName(updatedApplication.getCandidateName());
        existingApplication.setPositionApplied(updatedApplication.getPositionApplied());
        existingApplication.setCvUrl(updatedApplication.getCvUrl());
        existingApplication.setCoverLetter(updatedApplication.getCoverLetter());
        existingApplication.setExperience(updatedApplication.getExperience());
        existingApplication.setApplicationDate(updatedApplication.getApplicationDate());
        existingApplication.setEducationLevel(updatedApplication.getEducationLevel());
        existingApplication.setRelevent_experience(updatedApplication.isRelevent_experience());
        existingApplication.setStatus(updatedApplication.getStatus());

        // Sauvegarder les modifications
        ApplicationE updated = applicationService.saveApplication(existingApplication);

        return ResponseEntity.ok(updated); // Retourner l'application mise à jour
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getRecruitmentStatistics() {
        long totalApplications = applicationService.getTotalApplications();
        double acceptanceRate = applicationService.getAcceptanceRate();

        return ResponseEntity.ok(Map.of(
                "totalApplications", totalApplications,
                "acceptanceRate", acceptanceRate
        ));
    }
    @GetMapping("/history")
    public ResponseEntity<List<ApplicationE>> getRecruitmentHistory() {
        List<ApplicationE> history = applicationService.getRecruitmentHistory();
        return ResponseEntity.ok(history);
    }






}
