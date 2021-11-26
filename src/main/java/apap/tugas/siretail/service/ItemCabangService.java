package apap.tugas.siretail.service;

import apap.tugas.siretail.model.ItemCabangModel;

public interface ItemCabangService {
    ItemCabangModel getItemCabangById(int id);
    void deleteItemCabang(ItemCabangModel item);
}
