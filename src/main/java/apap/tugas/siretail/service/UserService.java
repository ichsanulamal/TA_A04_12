package apap.tugas.siretail.service;

import apap.tugas.siretail.model.UserModel;

public interface UserService {
    String encrypt(String password);
    String addUser(UserModel user);
    UserModel findUserByUsername(String username);
    String updateUserPassword(UserModel user, String newPassword);
}
