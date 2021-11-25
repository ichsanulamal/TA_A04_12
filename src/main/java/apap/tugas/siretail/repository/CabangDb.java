package apap.tugas.siretail.repository;

import apap.tugas.siretail.model.CabangModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CabangDb extends JpaRepository<CabangModel, Integer> {
    Optional<CabangModel> findById(int id);
}

