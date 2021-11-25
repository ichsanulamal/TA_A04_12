package apap.tugas.siretail.security;

import apap.tugas.siretail.model.UserModel;
import apap.tugas.siretail.repository.UserDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDb userDb;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userDb.findByUsername(username);
        Set<GrantedAuthority> grantedAuthorites = new HashSet<>();
        grantedAuthorites.add(new SimpleGrantedAuthority(user.getRole().getNama()));
        return new User(user.getUsername(), user.getPassword(),grantedAuthorites);
    }
}
