package apap.tugas.siretail.service;

import apap.tugas.siretail.model.CabangModel;

import java.util.List;

public interface CabangService {
    CabangModel getCabangByIdCabang(int id);
    List<CabangModel> getAllCabang();
    List<CabangModel> getAllCabangByManager();
    void deleteCabang(CabangModel cabang);
    void updateCabang(CabangModel cabang);
}
