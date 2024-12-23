package ma.emsi.interviewers.controller;


import ma.emsi.interviewers.Service.InterviewerService;
import ma.emsi.interviewers.model.Interviewer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviewers")
public class InterviewerController {

    private final InterviewerService interviewerService;

    public InterviewerController(InterviewerService interviewerService) {
        this.interviewerService = interviewerService;
    }

    // Ajouter un interviewer
    @PostMapping
    public ResponseEntity<Interviewer> addInterviewer(@RequestBody Interviewer interviewer) {
        Interviewer createdInterviewer = interviewerService.addInterviewer(interviewer);
        return ResponseEntity.ok(createdInterviewer);
    }

    // Récupérer tous les interviewers
    @GetMapping
    public ResponseEntity<List<Interviewer>> getAllInterviewers() {
        List<Interviewer> interviewers = interviewerService.getAllInterviewers();
        return ResponseEntity.ok(interviewers);
    }

    // Récupérer les interviewers disponibles
    @GetMapping("/available")
    public ResponseEntity<List<Interviewer>> getAvailableInterviewers() {
        List<Interviewer> availableInterviewers = interviewerService.getAvailableInterviewers();
        return ResponseEntity.ok(availableInterviewers);
    }

    // Mettre à jour un interviewer
    @PutMapping("/{id}")
    public ResponseEntity<Interviewer> updateInterviewer(
            @PathVariable Long id,
            @RequestBody Interviewer interviewer) {
        Interviewer updatedInterviewer = interviewerService.updateInterviewer(id, interviewer);
        return ResponseEntity.ok(updatedInterviewer);
    }

    // Supprimer un interviewer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterviewer(@PathVariable Long id) {
        interviewerService.deleteInterviewer(id);
        return ResponseEntity.noContent().build();
    }
}
