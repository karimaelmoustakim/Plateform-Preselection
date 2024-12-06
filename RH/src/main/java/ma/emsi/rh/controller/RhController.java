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

    // Endpoint pour récupérer toutes les candidatures
    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationDTO>> getAllApplications() {
        List<ApplicationDTO> applications = rhService.getAllApplications();
        return ResponseEntity.ok(applications);
    }

    // Endpoint pour modifier le statut d'une candidature
    @PutMapping("/applications/{id}/status")
    public ResponseEntity<Void> updateApplicationStatus(@PathVariable Long id, @RequestParam String status) {
        rhService.updateApplicationStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}
