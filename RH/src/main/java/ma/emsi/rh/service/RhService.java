package ma.emsi.rh.service;

import ma.emsi.rh.dto.ApplicationDTO;
import ma.emsi.rh.model.Interview;
import ma.emsi.rh.repo.InterviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RhService {
    private final InterviewRepository interviewRepository;
    private final RestTemplate restTemplate;



    public RhService(InterviewRepository interviewRepository, RestTemplate restTemplate) {
        this.interviewRepository = interviewRepository;
        this.restTemplate = restTemplate;
    }

    // Récupérer toutes les candidatures depuis le microservice Application
    public List<ApplicationDTO> getAllApplications() {
        String url = "http://localhost:8082/api/applications";
        ApplicationDTO[] applications = restTemplate.getForObject(url, ApplicationDTO[].class);
        if (applications == null) {
            throw new RuntimeException("Impossible de récupérer les candidatures.");
        }
        return Arrays.asList(applications);
    }

    // Récupérer les candidatures filtrées par statut
    public List<ApplicationDTO> getApplicationsByStatus(String status) {
        String url = "http://localhost:8082/api/applications/status?status=" + status;
        ApplicationDTO[] filteredApplications = restTemplate.getForObject(url, ApplicationDTO[].class);
        if (filteredApplications == null) {
            throw new RuntimeException("Aucune candidature trouvée pour le statut : " + status);
        }
        return Arrays.asList(filteredApplications);
    }

    // Envoyer les candidatures au service de prédiction
    public List<ApplicationDTO> sendApplicationsForPrediction() {
        String predictionUrl = "http://localhost:5000/api/predict";
        List<ApplicationDTO> allApplications = getAllApplications();

        System.out.println("Données envoyées à Flask : " + allApplications);

        try {
            ApplicationDTO[] predictions = restTemplate.postForObject(predictionUrl, allApplications, ApplicationDTO[].class);

            if (predictions == null) {
                throw new RuntimeException("Aucune prédiction reçue.");
            }
            // Mise à jour des statuts en fonction des prédictions
            for (ApplicationDTO application : predictions) {
                if ("Retenu".equals(application.getPrediction())) {
                    application.setStatus("ADMIS");
                } else {
                    application.setStatus("NON_ADMIS");
                }

                // Mettre à jour le statut dans la base de données via un appel PUT
                updateApplicationStatus(application.getId(), application.getStatus());
            }


            System.out.println("Réponse reçue de Flask : " + Arrays.toString(predictions));

            return Arrays.asList(predictions);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à Flask : " + e.getMessage());
            throw e;
        }
    }




    // Mettre à jour le statut d'une candidature
    public void updateApplicationStatus(Long applicationId, String newStatus) {
        String url = "http://localhost:8082/api/applications/" + applicationId + "/status";
        restTemplate.put(url, newStatus);
    }
    public ApplicationDTO getApplicationById(Long applicationId) {
        String url = "http://localhost:8082/api/applications/" + applicationId;
        return restTemplate.getForObject(url, ApplicationDTO.class);
    }


    // Envoyer une notification (simulateur pour le moment)
    public String sendNotification(Long applicationId) {
        // Récupérer la candidature par ID
        ApplicationDTO application = getApplicationById(applicationId); // Vous devez implémenter cette méthode.

        // Vérifier si la candidature existe
        if (application == null) {
            throw new RuntimeException("Candidature introuvable avec l'ID : " + applicationId);
        }

        // Récupérer le statut
        String status = application.getStatus();

        // Créer le message de notification
        String message = "Votre candidature (ID : " + applicationId + ") a été mise à jour avec le statut : " + status + ".";
        System.out.println("Notification envoyée : " + message);

        return message;
    }
    // Planifier un entities
    public Interview scheduleInterview(Long candidateId, LocalDateTime dateTime, String link, String interviewer) {
        Interview interview = new Interview();
        interview.setCandidateId(candidateId);
        interview.setInterviewDate(dateTime);
        interview.setInterviewLink(link);
        interview.setInterviewer(interviewer);

        return interviewRepository.save(interview);
    }
    // Récupérer tous les entretiens
    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    // Récupérer un entretien par ID
    public Interview getInterviewById(Long id) {
        Optional<Interview> interview = interviewRepository.findById(id);
        return interview.orElse(null);
    }
    public String sendInterviewNotification(Interview interview) {
        String message = "Bonjour, votre entretien est planifié pour " + interview.getInterviewDate() +
                ". Lien de visioconférence : " + interview.getInterviewLink();
        System.out.println("Notification envoyée au candidat ID " + interview.getCandidateId() + ": " + message);
        return message;
    }
    public Interview updateInterview(Long id, Long candidateId, LocalDateTime dateTime, String link, String interviewer) {
        // Vérifier si l'entretien existe
        Optional<Interview> existingInterview = interviewRepository.findById(id);
        if (existingInterview.isEmpty()) {
            throw new RuntimeException("Entretien introuvable avec l'ID : " + id);
        }

        // Mettre à jour les détails de l'entretien
        Interview interview = existingInterview.get();
        interview.setCandidateId(candidateId);
        interview.setInterviewDate(dateTime);
        interview.setInterviewLink(link);
        interview.setInterviewer(interviewer);

        // Sauvegarder les modifications
        return interviewRepository.save(interview);
    }
    public void deleteInterview(Long id) {
        // Vérifier si l'entretien existe
        if (!interviewRepository.existsById(id)) {
            throw new RuntimeException("Entretien introuvable avec l'ID : " + id);
        }

        // Supprimer l'entretien
        interviewRepository.deleteById(id);
    }



}
