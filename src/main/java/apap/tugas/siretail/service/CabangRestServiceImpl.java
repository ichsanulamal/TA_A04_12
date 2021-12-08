package apap.tugas.siretail.service;

import apap.tugas.siretail.model.CabangModel;
import apap.tugas.siretail.repository.CabangDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class CabangRestServiceImpl implements CabangRestService{
    @Autowired
    private CabangDb cabangDb;

    @Override
    public CabangModel getCabangById(int id) {
        Optional<CabangModel> cabang = cabangDb.findById(id);

        if(cabang.isPresent()) {
            return cabang.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<CabangModel> getListCabang() { return cabangDb.findAll(); }

    @Override
    public CabangModel requestCreateCabang(CabangModel cabang) {
        return cabangDb.save(cabang);
    }
}
