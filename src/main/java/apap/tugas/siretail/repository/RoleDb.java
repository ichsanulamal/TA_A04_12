package apap.tugas.siretail.repository;

import apap.tugas.siretail.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDb extends JpaRepository<RoleModel, Integer> {
}
