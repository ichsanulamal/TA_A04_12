package apap.tugas.siretail.service;

import apap.tugas.siretail.model.CabangModel;
import apap.tugas.siretail.model.ItemCabangModel;
import apap.tugas.siretail.repository.CabangDb;
import apap.tugas.siretail.repository.ItemCabangDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ItemCabangServiceImpl implements ItemCabangService {
    @Autowired
    CabangDb cabangDb;

    @Autowired
    ItemCabangDb itemCabangDb;

    public void deleteItemCabang(ItemCabangModel item){
        itemCabangDb.delete(item);
    }

    @Override
    public ItemCabangModel getItemCabangById(int id) {
        Optional<ItemCabangModel> item = itemCabangDb.findById(id);
        if (item.isPresent()) {
            return item.get();
        }
        return null;
    }
}
