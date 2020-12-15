package pl.olafszewczak.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.olafszewczak.dodohow.dtos.UserDto;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.repositories.UserRepository;

@Service
public class DtoMapper {
    private UserRepository userRepository;

    @Autowired
    public DtoMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User map (UserDto userDto){
        return new User(userDto.getId(), userDto.getLogin(), userDto.getPassword(), userDto.getEmail());
    }

    public UserDto map (User user){
        return new UserDto(user.getId(), user.getLogin(), user.getPassword(), user.getEmail());
    }
}
