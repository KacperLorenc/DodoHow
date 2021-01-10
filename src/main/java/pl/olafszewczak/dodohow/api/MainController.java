package pl.olafszewczak.dodohow.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.olafszewczak.dodohow.dtos.UserDto;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.repositories.UserRepository;
import pl.olafszewczak.dodohow.security.IAuthenticationFacade;
import pl.olafszewczak.dodohow.services.DtoMapper;

import java.util.Optional;

@Controller
@RequestMapping("/api")
public class MainController {
    private DtoMapper dtoMapper;
    private IAuthenticationFacade authenticationFacade;
    private UserRepository userRepository;

    @Autowired
    public MainController(DtoMapper dtoMapper, IAuthenticationFacade authenticationFacade, UserRepository userRepository) {
        this.dtoMapper = dtoMapper;
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
    }

    @GetMapping("/sections")
    public ResponseEntity<HttpHeaders> getSections() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "localhost:3000/MainPage");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/session")
    public ResponseEntity<UserDto> getUserFromSession() {
        try {
            Authentication authentication = authenticationFacade.getAuthentication();
            Optional<User> currentUser = userRepository.findByUsername(authentication.getName());
            if (currentUser.isPresent()) {
                UserDto userDto = dtoMapper.map(currentUser.get());
                return ResponseEntity.ok(userDto);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
