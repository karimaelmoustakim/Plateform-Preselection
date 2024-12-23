package ma.emsi.application.controller;

import ma.emsi.application.module.ApplicationE;
import ma.emsi.application.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ApplicationService applicationService;

    public DashboardController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Récupérer les statistiques générales
    @GetMapping("/statistics")
    public ResponseEntity<?> getRecruitmentStatistics() {
        long totalApplications = applicationService.getTotalApplications();
        double acceptanceRate = applicationService.getAcceptanceRate();

        return ResponseEntity.ok(Map.of(
                "totalApplications", totalApplications,
                "acceptanceRate", acceptanceRate
        ));
    }

    // Récupérer l'historique des recrutements
    @GetMapping("/history")
    public ResponseEntity<List<ApplicationE>> getRecruitmentHistory() {
        List<ApplicationE> history = applicationService.getRecruitmentHistory();
        return ResponseEntity.ok(history);
    }

}
