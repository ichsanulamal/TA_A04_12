package apap.tugas.siretail.service;

import apap.tugas.siretail.model.RoleModel;
import apap.tugas.siretail.repository.RoleDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDb roleDb;

    @Override
    public List<RoleModel> findAll() {
        return roleDb.findAll();
    }

    @Override
    public RoleModel findById(int id) {
        return roleDb.findById(id);
    }
}
