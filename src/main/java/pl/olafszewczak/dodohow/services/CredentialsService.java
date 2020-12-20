package pl.olafszewczak.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.olafszewczak.dodohow.dtos.UserDto;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.repositories.TokenRepository;
import pl.olafszewczak.dodohow.repositories.UserRepository;
import pl.olafszewczak.dodohow.security.VerificationToken;

import java.util.Optional;

@Service
public class CredentialsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenRepository tokenRepository;
    private DtoMapper mapper;

    @Autowired
    public CredentialsService(UserRepository userRepository, DtoMapper mapper, PasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public boolean registerUser(UserDto userDto) {
        if (!checkRegister(userDto)) {
            User user = mapper.map(userDto);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles("ROLE_USER");
            user.setActive(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean checkRegister(UserDto user) {
        return userRepository.existsByEmail(user.getEmail()) ||
                userRepository.existsByUsername(user.getLogin());
    }

    public Optional<User> getUser(String verificationToken) {
        Optional<VerificationToken> token = tokenRepository.findByToken(verificationToken);
        return token.map(VerificationToken::getUser);
    }

    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    public Optional<VerificationToken> getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    public Optional<User> getByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
