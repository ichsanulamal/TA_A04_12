package apap.tugas.siretail.repository;

import apap.tugas.siretail.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDb extends JpaRepository<UserModel, Integer> {
    UserModel findByUsername(String username);
}
