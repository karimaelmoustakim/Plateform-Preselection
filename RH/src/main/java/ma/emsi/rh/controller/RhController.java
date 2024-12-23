package ma.emsi.rh.controller;

import ma.emsi.rh.dto.ApplicationDTO;
import ma.emsi.rh.service.RhService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rh")

public class RhController {

    private final RhService rhService;

    public RhController(RhService rhService) {
        this.rhService = rhService;
    }

    // Récupérer toutes les candidatures
    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationDTO>> getAllApplications() {
        List<ApplicationDTO> applications = rhService.getAllApplications();
        return ResponseEntity.ok(applications);
    }

    // Visualiser les candidatures admises
    @GetMapping("/applications/admitted")
    public ResponseEntity<List<ApplicationDTO>> getAdmittedApplications() {
        // Faire la prédiction sur toutes les candidatures
        List<ApplicationDTO> predictions = rhService.sendApplicationsForPrediction();

        // Filtrer les candidatures avec le statut "Admis"
        List<ApplicationDTO> admittedApplications = predictions.stream()
                .filter(application -> "ADMIS".equalsIgnoreCase(application.getStatus()))
                .toList();

        return ResponseEntity.ok(admittedApplications);
    }


    // Visualiser les candidatures non admises
    @GetMapping("/applications/not-admitted")
    public ResponseEntity<List<ApplicationDTO>> getNotAdmittedApplications() {
        // Faire la prédiction sur toutes les candidatures
        List<ApplicationDTO> predictions = rhService.sendApplicationsForPrediction();

        // Filtrer les candidatures avec le statut "Admis"
        List<ApplicationDTO> admittedApplications = predictions.stream()
                .filter(application -> "NON_ADMIS".equalsIgnoreCase(application.getStatus()))
                .toList();

        return ResponseEntity.ok(admittedApplications);
    }

    // Envoyer les candidatures au service de prédiction
    @PostMapping("/predict")
    public ResponseEntity<List<ApplicationDTO>> predictApplications() {
        List<ApplicationDTO> predictions = rhService.sendApplicationsForPrediction();
        predictions.forEach(prediction -> System.out.println("Prédiction : " + prediction));
        return ResponseEntity.ok(predictions);
    }

    // Mettre à jour le statut d'une candidature
    @PutMapping("/applications/{id}/status")
    public ResponseEntity<Void> updateApplicationStatus(@PathVariable Long id, @RequestParam String status) {
        rhService.updateApplicationStatus(id, status);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/applications/{id}/notify")
    public ResponseEntity<String> notifyCandidate(@PathVariable Long id) {
        String notificationMessage = rhService.sendNotification(id);
        return ResponseEntity.ok(notificationMessage);
    }
}
