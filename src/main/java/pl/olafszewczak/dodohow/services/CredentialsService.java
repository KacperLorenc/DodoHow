package pl.olafszewczak.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.olafszewczak.dodohow.dtos.UserDto;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.repositories.UserRepository;

@Service
public class CredentialsService {

    private UserRepository userRepository;

    @Autowired
    public CredentialsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(UserDto userDto){

        //walidation logic

        User user = new User();

        return userRepository.save(user);
    }
}
