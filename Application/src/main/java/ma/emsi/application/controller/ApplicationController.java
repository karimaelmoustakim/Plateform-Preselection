package ma.emsi.application.controller;

import ma.emsi.application.module.ApplicationE;
import ma.emsi.application.module.ApplicationStatus;
import ma.emsi.application.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/my-applications")
    public ResponseEntity<List<ApplicationE>> getMyApplications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("Authentication is null or not authenticated.");
            return ResponseEntity.status(403).body(null);
        }

        System.out.println("Authenticated user: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());

        String candidateEmail = authentication.getName();
        Long candidateId = applicationService.getCandidateIdByEmail(candidateEmail);

        if (candidateId == null) {
            System.out.println("Candidate ID not found for email: " + candidateEmail);
            return ResponseEntity.status(403).body(null);
        }

        List<ApplicationE> applications = applicationService.getApplicationsByCandidate(candidateId);
        return ResponseEntity.ok(applications);
    }



    @GetMapping
    public ResponseEntity<List<ApplicationE>> getAllApplications() {
        List<ApplicationE> applications = applicationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }


    // Récupérer une candidature par ID (vérification de l'accès)
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationE> getApplicationById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String candidateEmail = authentication.getName();
        Long candidateId = applicationService.getCandidateIdByEmail(candidateEmail);

        ApplicationE application = applicationService.getApplicationById(id);
        if (!application.getCandidateId().equals(candidateId)) {
            return ResponseEntity.status(403).body(null); // Forbidden
        }

        return ResponseEntity.ok(application);
    }

    // Supprimer une candidature (protection par ID du candidat)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String candidateEmail = authentication.getName();
        Long candidateId = applicationService.getCandidateIdByEmail(candidateEmail);

        ApplicationE application = applicationService.getApplicationById(id);
        if (!application.getCandidateId().equals(candidateId)) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationE> updateStatus(@PathVariable Long id, @RequestBody String status) {
        ApplicationE application = applicationService.getApplicationById(id);
        application.setStatus(ApplicationStatus.valueOf(status.toUpperCase()));
        ApplicationE updatedApplication = applicationService.createApplication(application);
        return ResponseEntity.ok(updatedApplication);
    }

    @GetMapping("/debug-session")
    public ResponseEntity<String> debugSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Authenticated user: " + authentication.getName() + ", Authorities: " + authentication.getAuthorities());
    }

}
