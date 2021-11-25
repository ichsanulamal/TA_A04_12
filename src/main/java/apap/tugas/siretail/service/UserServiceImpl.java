package apap.tugas.siretail.service;

import apap.tugas.siretail.model.UserModel;
import apap.tugas.siretail.repository.UserDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDb userDb;

    @Override
    public UserModel findUserByUsername(String username) {
        return userDb.findByUsername(username);
    }

    @Override
    public String addUser(UserModel user) {
        // username check
        if (findUserByUsername(user.getUsername()) != null) {
            return "Username sudah digunakan, mohon gunakan username lain";
        }

        // password check
        String pass = user.getPassword();
        if (isValidPassword(pass)) {
            String password = encrypt(pass);
            user.setPassword(password);
            userDb.save(user);
            return "User berhasil ditambahkan!";
        }
        return "Password tidak sesuai ketentuan, mohon ulangi";
    }

    @Override
    public String updateUserPassword(UserModel user, String newPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encUserPassword = user.getPassword();
        if (encoder.matches(newPassword, encUserPassword)) {
            return "Mohon gunakan password yang berbeda";
        }

        if (isValidPassword(newPassword)) {
            String password = encrypt(newPassword);
            user.setPassword(password);
            userDb.save(user);
            return "Password berhasil diubah!";
        }
        return "Password tidak sesuai ketentuan, mohon ulangi";
    }

    private boolean isValidPassword(String pass) {
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        if (pass.length() >= 8 && pass.matches(regex)){
            return true;
        }
        return false;
    }

    @Override
    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }


}