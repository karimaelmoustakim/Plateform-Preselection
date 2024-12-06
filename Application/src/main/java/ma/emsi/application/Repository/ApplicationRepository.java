package ma.emsi.application.Repository;

import ma.emsi.application.module.ApplicationE;
import ma.emsi.application.module.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationE, Long> {

    // Récupérer toutes les candidatures pour un candidat spécifique
    List<ApplicationE> findByCandidateId(Long candidateId);
    List<ApplicationE> findByStatus(ApplicationStatus status);

}
