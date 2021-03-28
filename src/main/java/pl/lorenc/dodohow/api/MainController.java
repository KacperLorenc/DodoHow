package pl.lorenc.dodohow.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.lorenc.dodohow.dtos.ScoreDto;
import pl.lorenc.dodohow.dtos.UserDto;
import pl.lorenc.dodohow.entities.Score;
import pl.lorenc.dodohow.entities.User;
import pl.lorenc.dodohow.services.DtoMapper;
import pl.lorenc.dodohow.services.ScoreService;
import pl.lorenc.dodohow.services.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/api")
public class MainController {
    private DtoMapper dtoMapper;
    private UserService userService;
    private ScoreService scoreService;

    @Autowired
    public MainController(DtoMapper dtoMapper, UserService userService, ScoreService scoreService) {
        this.dtoMapper = dtoMapper;
        this.userService = userService;
        this.scoreService = scoreService;
    }

    @GetMapping("/quizzes")
    public ResponseEntity<HttpHeaders> getQuizzes() {
        try {
            Optional<User> currentUser = userService.getUserFromSession();
            if (currentUser.isPresent()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", "localhost:3000");
                headers.add("UserId", currentUser.get().getId().toString());
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        try {
            Optional<User> user = userService.findById(id);
            return user.map(value -> ResponseEntity.ok(dtoMapper.map(value))).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
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

    @PostMapping("/score")
    public String addScore(@RequestBody ScoreDto scoreDto) {
        try {
            Score score = dtoMapper.map(scoreDto);
            if(score!= null){
                scoreService.saveScore(score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "home/index";
    }
}
