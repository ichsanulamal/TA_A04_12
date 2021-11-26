package apap.tugas.siretail.service;

import apap.tugas.siretail.model.ItemCabangModel;
import apap.tugas.siretail.rest.SiItemModel;

import java.util.List;

public interface ItemRestService {
    void addItem(int idCabang, SiItemModel item);
    List<SiItemModel> getListItemFromSiItem();
}
