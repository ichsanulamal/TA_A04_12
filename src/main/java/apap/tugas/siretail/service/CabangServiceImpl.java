package apap.tugas.siretail.service;

import apap.tugas.siretail.model.CabangModel;
import apap.tugas.siretail.model.ItemCabangModel;
import apap.tugas.siretail.repository.CabangDb;
import apap.tugas.siretail.repository.ItemCabangDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CabangServiceImpl implements CabangService {
    @Autowired
    CabangDb cabangDb;

//    @Autowired
//    ItemCabangDb itemCabangDb;

    @Override
    public void addCabang(CabangModel cabang){
        cabangDb.save(cabang);
    }

    @Override
    public CabangModel getCabangByIdCabang(int id) {
        Optional<CabangModel> cabang = cabangDb.findById(id);
        if (cabang.isPresent()) {
            return cabang.get();
        }
        return null;
    }

    @Override
    public List<CabangModel> getAllCabang(){
        return cabangDb.findAll();
    }

    @Override
    public List<CabangModel> getAllCabangByManager(String username, List<CabangModel> dataCabang){
        List<CabangModel> listCabangManager = new ArrayList<CabangModel>();
        for (CabangModel cabang : dataCabang) {
            System.out.println(username);
            System.out.println(cabang.getPenanggungJawab().getUsername().equals(username));
            if (cabang.getPenanggungJawab().getUsername().equals(username)) {
                listCabangManager.add(cabang);
            }
        }
        return listCabangManager;
    }

    @Override
    public void deleteCabang(CabangModel cabang){
        cabangDb.delete(cabang);
    }

    @Override
    public void updateCabang(CabangModel cabang) {
        cabangDb.save(cabang);
    }
}
