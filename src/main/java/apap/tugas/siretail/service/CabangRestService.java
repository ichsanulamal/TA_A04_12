package apap.tugas.siretail.service;

import apap.tugas.siretail.model.CabangModel;

import java.util.List;

public interface CabangRestService {
    CabangModel getCabangById(int id);
    List<CabangModel> getListCabang();
}
