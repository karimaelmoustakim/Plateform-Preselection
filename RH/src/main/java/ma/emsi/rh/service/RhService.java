package ma.emsi.rh.service;

import ma.emsi.rh.dto.ApplicationDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class RhService {

    private final RestTemplate restTemplate;

    public RhService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Récupérer toutes les candidatures depuis le microservice Application
    public List<ApplicationDTO> getAllApplications() {
        String url = "http://application-service/api/applications"; // URL du microservice Application
        ApplicationDTO[] applications = restTemplate.getForObject(url, ApplicationDTO[].class);
        if (applications == null) {
            throw new RuntimeException("Impossible de récupérer les candidatures.");
        }
        return Arrays.asList(applications);
    }

    // Récupérer une candidature spécifique par ID
    public ApplicationDTO getApplicationById(Long applicationId) {
        String url = "http://application-service/api/applications/" + applicationId;
        return restTemplate.getForObject(url, ApplicationDTO.class);
    }

    // Modifier le statut d'une candidature
    public void updateApplicationStatus(Long applicationId, String newStatus) {
        String url = "http://application-service/api/applications/" + applicationId + "/status";
        restTemplate.put(url, newStatus);
    }

    // Filtrer les candidatures par statut (Admis, Non Admis, En Attente)
    public List<ApplicationDTO> getApplicationsByStatus(String status) {
        String url = "http://application-service/api/applications?status=" + status;
        ApplicationDTO[] applications = restTemplate.getForObject(url, ApplicationDTO[].class);
        if (applications == null) {
            throw new RuntimeException("Aucune candidature trouvée pour le statut : " + status);
        }
        return Arrays.asList(applications);
    }
}
