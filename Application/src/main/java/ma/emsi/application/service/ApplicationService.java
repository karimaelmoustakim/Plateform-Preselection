package ma.emsi.application.service;

import ma.emsi.application.Repository.ApplicationRepository;
import ma.emsi.application.module.ApplicationE;
import ma.emsi.application.module.ApplicationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApplicationService {
    private static final Logger log = LoggerFactory.getLogger(ApplicationService.class);


    private final ApplicationRepository applicationRepository;
//    private final RestTemplate restTemplate;

    private final String userServiceUrl = "http://localhost:8081/api/auth/exists"; // URL de base du microservice utilisateur

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
//        this.restTemplate = restTemplate;
    }

    // Vérifier si un utilisateur existe dans le microservice utilisateur
//    private boolean checkUserExists(Long candidateId) {
//        String url = userServiceUrl + "/" + candidateId; // URL pour vérifier l'existence
//        try {
//            return restTemplate.getForObject(url, Boolean.class); // Appel au microservice
//        } catch (Exception e) {
//            return false; // En cas d'échec, on considère que l'utilisateur n'existe pas
//        }
//    }

    // Créer une nouvelle candidature
    public ApplicationE createApplication(ApplicationE application) {
        // Vérifier si le candidat existe
//        if (!checkUserExists(application.getCandidateId())) {
//            throw new RuntimeException("Le candidat avec l'ID " + application.getCandidateId() + " n'existe pas !");
//        }

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
//    public Long getCandidateIdByEmail(String email) {
//        String url = "http://localhost:8080/api/auth/email/" + email;
//
//        try {
//            // Effectuer la requête GET avec le RestTemplate configuré
//            ResponseEntity<Long> response = restTemplate.getForEntity(url, Long.class);
//
//            // Retourner l'ID du candidat si la réponse est valide
//            return response.getBody();
//        } catch (HttpClientErrorException.Unauthorized e) {
//            // Gérer les erreurs d'authentification
//            log.error("Accès non autorisé à l'URL {}: {}", url, e.getMessage());
//            throw new RuntimeException("Non autorisé : vérifiez vos informations d'identification.", e);
//        } catch (HttpClientErrorException.NotFound e) {
//            // Gérer les cas où le candidat n'est pas trouvé
//            log.error("Candidat introuvable pour l'email {}: {}", email, e.getMessage());
//            return null; // Retourner null ou lancer une exception selon votre logique
//        } catch (HttpClientErrorException e) {
//            // Gérer les autres erreurs HTTP
//            log.error("Erreur HTTP lors de l'accès à l'URL {}: {}", url, e.getMessage());
//            throw e;
//        }
//    }

    public List<ApplicationE> getAllApplications() {
        return applicationRepository.findAll(); // Utilise JpaRepository pour récupérer toutes les applications
    }

    public ApplicationE saveApplication(ApplicationE application) {
        log.info("Sauvegarde de l'application avec ID : {}", application.getId());
        ApplicationE savedApplication = applicationRepository.save(application);
        log.info("Application sauvegardée avec succès : {}", savedApplication);
        return savedApplication;
    }
    public List<ApplicationE> getApplicationsByStatus(ApplicationStatus status) {
        return applicationRepository.findByStatus(status);
    }
    public long getTotalApplications() {
        return applicationRepository.count(); // Utilise JpaRepository pour compter toutes les candidatures
    }
    public double getAcceptanceRate() {
        long totalApplications = applicationRepository.count();
        long acceptedApplications = applicationRepository.countByStatus(ApplicationStatus.ADMIS); // Statut "ADMIS"
        return totalApplications == 0 ? 0.0 : ((double) acceptedApplications / totalApplications) * 100;
    }
    public List<ApplicationE> getRecruitmentHistory() {
        return applicationRepository.findAllByOrderByApplicationDateDesc(); // Tri par date descendante
    }






}
