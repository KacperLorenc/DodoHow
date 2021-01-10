package pl.olafszewczak.dodohow.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.olafszewczak.dodohow.dtos.UserDto;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.services.UserService;
import pl.olafszewczak.dodohow.services.DtoMapper;

import java.util.Optional;

@Controller
@RequestMapping("/api")
public class MainController {
    private DtoMapper dtoMapper;
    private UserService userService;

    @Autowired
    public MainController(DtoMapper dtoMapper, UserService userService) {
        this.dtoMapper = dtoMapper;
        this.userService = userService;
    }

    @GetMapping("/sections")
    public ResponseEntity<HttpHeaders> getSections() {
        try {
            Optional<User> currentUser = userService.getUserFromSession();
            if (currentUser.isPresent()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", "localhost:3000");
                headers.add("UserId", currentUser.get().getId().toString());
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping ("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        try {
            Optional<User> user = userService.getById(id);
            return user.map(value -> ResponseEntity.ok(dtoMapper.map(value))).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/session")
    public ResponseEntity<UserDto> getUserFromSession() {
        try {
            Optional<User> currentUser = userService.getUserFromSession();
            if (currentUser.isPresent()) {
                UserDto userDto = dtoMapper.map(currentUser.get());
                return ResponseEntity.ok(userDto);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
