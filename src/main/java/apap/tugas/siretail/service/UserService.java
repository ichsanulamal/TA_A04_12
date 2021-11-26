package apap.tugas.siretail.service;

import apap.tugas.siretail.model.UserModel;
import java.util.List;

public interface UserService {
    String encrypt(String password);
    String addUser(UserModel user);
    UserModel findUserByUsername(String username);
    UserModel findById(String id);
    String updateUserPassword(UserModel user, String newPassword);
    List<UserModel> retrieveAllUser();
    String editUser(UserModel user);

}
