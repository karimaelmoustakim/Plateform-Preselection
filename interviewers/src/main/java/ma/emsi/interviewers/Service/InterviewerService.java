package ma.emsi.interviewers.Service;


import ma.emsi.interviewers.Repo.InterviewerRepository;
import ma.emsi.interviewers.model.Interviewer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewerService {

    private final InterviewerRepository interviewerRepository;

    public InterviewerService(InterviewerRepository interviewerRepository) {
        this.interviewerRepository = interviewerRepository;
    }

    // Ajouter un interviewer
    public Interviewer addInterviewer(Interviewer interviewer) {
        return interviewerRepository.save(interviewer);
    }

    // Récupérer tous les interviewers
    public List<Interviewer> getAllInterviewers() {
        return interviewerRepository.findAll();
    }

    // Récupérer les interviewers disponibles
    public List<Interviewer> getAvailableInterviewers() {
        return interviewerRepository.findByIsAvailable(true);
    }

    // Mettre à jour un interviewer
    public Interviewer updateInterviewer(Long id, Interviewer updatedInterviewer) {
        Interviewer existingInterviewer = interviewerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interviewer introuvable avec l'ID : " + id));

        existingInterviewer.setName(updatedInterviewer.getName());
        existingInterviewer.setEmail(updatedInterviewer.getEmail());
        existingInterviewer.setSpecialty(updatedInterviewer.getSpecialty());
        existingInterviewer.setAvailable(updatedInterviewer.isAvailable());

        return interviewerRepository.save(existingInterviewer);
    }

    // Supprimer un interviewer
    public void deleteInterviewer(Long id) {
        interviewerRepository.deleteById(id);
    }
}
