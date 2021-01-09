package pl.olafszewczak.dodohow.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.olafszewczak.dodohow.entities.User;

@Service
public class DbInit implements CommandLineRunner {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DbInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        System.out.println("--------------------------------------------------------------------------------------------");
        User user = new User(null,"kapa123",passwordEncoder.encode("kapa123"),"kacperlorenc1@wp.pl",true,"ROLE_USER");
        userRepository.save(user);

        userRepository.findAll().forEach(u -> {
            System.out.println("login:" + u.getUsername() + " " + "password:" + passwordEncoder.encode(u.getPassword()));
        });
    }
}
