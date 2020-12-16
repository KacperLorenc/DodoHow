package pl.olafszewczak.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.olafszewczak.dodohow.dtos.UserDto;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.repositories.UserRepository;

@Service
public class CredentialsService {

    private UserRepository userRepository;
    private DtoMapper mapper;


    @Autowired
    public CredentialsService(UserRepository userRepository, DtoMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public boolean registerUser(UserDto userDto) {
        if (!checkRegister(userDto)) {
            User user = mapper.map(userDto);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean loginUser(UserDto userDto){
        if(checkLogin(userDto)){
            //login logic
            return true;
        }
        return false;
    }

    public boolean checkLogin(UserDto user) {
        return userRepository
                .existsByEmail(user.getEmail());
    }

    public boolean checkRegister(UserDto user) {
        return userRepository.existsByEmail(user.getEmail()) ||
                userRepository.existsByLogin(user.getLogin());
    }
}
