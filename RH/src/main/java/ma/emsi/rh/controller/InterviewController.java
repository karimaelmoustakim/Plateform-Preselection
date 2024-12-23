package ma.emsi.rh.controller;

import ma.emsi.rh.model.Interview;
import ma.emsi.rh.service.RhService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/rh/interviews")
public class InterviewController {

    private final RhService rhService;

    public InterviewController(RhService rhService) {
        this.rhService = rhService;
    }

    /**
     * Récupérer tous les entretiens
     */
    @GetMapping
    public ResponseEntity<List<Interview>> getAllInterviews() {
        List<Interview> interviews = rhService.getAllInterviews();
        return ResponseEntity.ok(interviews);
    }

    /**
     * Récupérer un entretien par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Interview> getInterviewById(@PathVariable Long id) {
        Interview interview = rhService.getInterviewById(id);
        if (interview == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(interview);
    }

    /**
     * Planifier un entretien
     */
    @PostMapping
    public ResponseEntity<Interview> scheduleInterview(@RequestBody Interview request) {
        // Planifier un nouvel entretien
        Interview interview = rhService.scheduleInterview(
                request.getCandidateId(),
                request.getInterviewDate(),
                request.getInterviewLink(),
                request.getInterviewer()
        );
        return ResponseEntity.ok(interview);
    }


    /**
     * Envoyer une notification pour un entretien
     */
    @PostMapping("/{id}/notify")
    public ResponseEntity<String> notifyInterview(@PathVariable Long id) {
        Interview interview = rhService.getInterviewById(id);

        if (interview == null) {
            return ResponseEntity.notFound().build();
        }

        String notificationMessage = rhService.sendInterviewNotification(interview);
        return ResponseEntity.ok(notificationMessage);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Interview> updateInterview(
            @PathVariable Long id,
            @RequestBody Interview updatedInterviewRequest
    ) {
        // Mettre à jour l'entretien avec les nouvelles informations
        Interview updatedInterview = rhService.updateInterview(
                id,
                updatedInterviewRequest.getCandidateId(),
                updatedInterviewRequest.getInterviewDate(),
                updatedInterviewRequest.getInterviewLink(),
                updatedInterviewRequest.getInterviewer()
        );

        return ResponseEntity.ok(updatedInterview);
    }

    // Supprimer un entretien
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id) {
        rhService.deleteInterview(id);
        return ResponseEntity.noContent().build();
    }


}
