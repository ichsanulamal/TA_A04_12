package apap.tugas.siretail.service;

import apap.tugas.siretail.model.ItemCabangModel;
import apap.tugas.siretail.rest.SiItemModel;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ItemRestService {
    void addItem(int idCabang, SiItemModel item);
    SiItemModel getSiItemModel(String uuid);
    List<SiItemModel> getListItemFromSiItem();
    ResponseEntity<String> getListSiItem();
}
