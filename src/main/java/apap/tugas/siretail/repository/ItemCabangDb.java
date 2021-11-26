package apap.tugas.siretail.repository;

import apap.tugas.siretail.model.ItemCabangModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCabangDb extends JpaRepository<ItemCabangModel, Integer> {
    ItemCabangModel getByItemID(String itemID);
    Optional<ItemCabangModel> findById(int id);
}
