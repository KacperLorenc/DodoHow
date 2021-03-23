package pl.lorenc.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lorenc.dodohow.dtos.UserDto;
import pl.lorenc.dodohow.repositories.TokenRepository;
import pl.lorenc.dodohow.entities.User;
import pl.lorenc.dodohow.repositories.UserRepository;
import pl.lorenc.dodohow.security.IAuthenticationFacade;
import pl.lorenc.dodohow.security.VerificationToken;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenRepository tokenRepository;
    private IAuthenticationFacade authenticationFacade;
    private DtoMapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, DtoMapper mapper, PasswordEncoder passwordEncoder, TokenRepository tokenRepository, IAuthenticationFacade authenticationFacade) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.authenticationFacade = authenticationFacade;
    }

    @Transactional
    public boolean registerUser(UserDto userDto) {
        if (!checkRegister(userDto)) {
            User user = mapper.map(userDto);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if(userDto.getTeacher() == null || !userDto.getTeacher())
                user.setRoles("ROLE_USER");
            else
                user.setRoles("ROLE_TEACHER");
            user.setActive(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public Optional<User> getUserFromSession(){
        Authentication authentication = authenticationFacade.getAuthentication();
        return userRepository.findByUsername(authentication.getName());
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

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }
}
