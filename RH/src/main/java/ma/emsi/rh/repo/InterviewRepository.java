package ma.emsi.rh.repo;

import ma.emsi.rh.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    // Vous pouvez ajouter des méthodes spécifiques si nécessaire, par exemple :
    // List<Interview> findByCandidateId(Long candidateId);
}
