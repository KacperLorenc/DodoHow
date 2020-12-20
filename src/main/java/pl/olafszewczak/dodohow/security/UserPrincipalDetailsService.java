package pl.olafszewczak.dodohow.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserPrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Optional<User> userOptional =  userRepository.findByUsername(s);
        return userOptional.map(UserPrincipal::new).orElse(null);
    }
}
