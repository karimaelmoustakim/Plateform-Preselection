package ma.emsi.interviewers.Repo;

import ma.emsi.interviewers.model.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {
    List<Interviewer> findByIsAvailable(boolean isAvailable);
    List<Interviewer> findBySpecialty(String specialty);
}
