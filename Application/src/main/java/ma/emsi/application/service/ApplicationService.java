package ma.emsi.application.service;

import ma.emsi.application.Repository.ApplicationRepository;
import ma.emsi.application.module.ApplicationE;
import ma.emsi.application.module.ApplicationStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final RestTemplate restTemplate;

    private final String userServiceUrl = "http://localhost:8081/api/auth/exists"; // URL de base du microservice utilisateur

    public ApplicationService(ApplicationRepository applicationRepository, RestTemplate restTemplate) {
        this.applicationRepository = applicationRepository;
        this.restTemplate = restTemplate;
    }

    // Vérifier si un utilisateur existe dans le microservice utilisateur
    private boolean checkUserExists(Long candidateId) {
        String url = userServiceUrl + "/" + candidateId; // URL pour vérifier l'existence
        try {
            return restTemplate.getForObject(url, Boolean.class); // Appel au microservice
        } catch (Exception e) {
            return false; // En cas d'échec, on considère que l'utilisateur n'existe pas
        }
    }

    // Créer une nouvelle candidature
    public ApplicationE createApplication(ApplicationE application) {
        // Vérifier si le candidat existe
        if (!checkUserExists(application.getCandidateId())) {
            throw new RuntimeException("Le candidat avec l'ID " + application.getCandidateId() + " n'existe pas !");
        }

        application.setApplicationDate(LocalDate.now()); // Définir automatiquement la date de candidature
        application.setStatus(ApplicationStatus.EN_ATTENTE); // Statut par défaut
        return applicationRepository.save(application);
    }

    // Récupérer toutes les candidatures pour un candidat spécifique
    public List<ApplicationE> getApplicationsByCandidate(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId);
    }

    // Récupérer une candidature spécifique par ID
    public ApplicationE getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée !"));
    }

    // Supprimer une candidature par ID
    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
    public Long getCandidateIdByEmail(String email) {
        String url = "http://localhost:8081/api/auth/email/" + email; // Adapter si nécessaire
        ResponseEntity<Long> response = restTemplate.getForEntity(url, Long.class);
        return response.getBody();
    }
    public List<ApplicationE> getAllApplications() {
        return applicationRepository.findAll(); // Utilise JpaRepository pour récupérer toutes les applications
    }

}
