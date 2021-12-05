package apap.tugas.siretail.service;

import apap.tugas.siretail.rest.SiItemModel;

import java.util.List;

public interface ItemRestService {
    Object addItem(int idCabang, SiItemModel item);
    SiItemModel getSiItemModel(String uuid);
    List<SiItemModel> getListItemFromSiItem();
}
