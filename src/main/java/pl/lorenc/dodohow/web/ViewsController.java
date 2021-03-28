package pl.lorenc.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.lorenc.dodohow.dtos.TeachersSetDto;
import pl.lorenc.dodohow.dtos.UserDto;
import pl.lorenc.dodohow.entities.Quiz;
import pl.lorenc.dodohow.services.DtoMapper;
import pl.lorenc.dodohow.services.ScoreService;
import pl.lorenc.dodohow.services.QuizService;
import pl.lorenc.dodohow.services.UserService;
import pl.lorenc.dodohow.entities.User;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ViewsController {

    private UserService userService;
    private QuizService quizService;
    private ScoreService scoreService;
    private DtoMapper mapper;

    @Autowired
    public ViewsController(UserService userService, QuizService quizService, ScoreService scoreService, DtoMapper mapper) {
        this.userService = userService;
        this.quizService = quizService;
        this.scoreService = scoreService;
        this.mapper = mapper;
    }

    @GetMapping("/")
    public String getHome() {
        return "home/index";
    }

    @GetMapping("/quizzes")
    public String getQuizzes(Model model) {
        try {
            Optional<User> userOptional = userService.getUserFromSession();
            return userOptional.map(user -> {
                Set<Quiz> userQuizs = quizService.findUsersQuizzes(user); //znajduje sekcje uzytkownika
                if (userQuizs.isEmpty()) {
                    quizService.getFirstQuiz().ifPresent(userQuizs::add); //jesli nie ma zadnych przerobionych to przypisuje pierwsza sekcje
                } else {
                    quizService.findNextQuiz(user).ifPresent(userQuizs::add); //jesli ma jakas przerobiona to dodaje kolejna
                }
                user.setQuizzes(userQuizs);
                userService.saveRegisteredUser(user);
                model.addAttribute("user", mapper.map(user));
                model.addAttribute("quizzes", userQuizs.stream().sorted(Comparator.comparing(Quiz::getNumberInClass)).map(quiz -> mapper.map(quiz, user)).collect(Collectors.toList()));
                model.addAttribute("scores", scoreService.getScores(user));
                return "quizzes/quizzes";
            }).orElse("redirect:/login");
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/quizzes/quiz/{id}")
    public String getquiz(Model model, @PathVariable Long id) {
        try {
            Optional<User> userOptional = userService.getUserFromSession();
            return userOptional.map(user -> {
                if (quizService.existsById(id)) {
                    model.addAttribute("quiz", mapper.mapToQuiz(user, id));
                    return "quizzes/quiz";         //jeśli znalazło sekcje i uzytkownika to zwraca dobry template
                } else {
                    return "redirect:/";
                }
            }).orElse("redirect:/login");

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/teachers")
    public String getTeachers(Model model) {
        try {

            Set<UserDto> teachers = userService.findUsersBy(false, "ROLE_TEACHER")
                    .stream()
                    .map(mapper::map)
                    .collect(Collectors.toSet());
            model.addAttribute("teacherSet", new TeachersSetDto(teachers));
            model.addAttribute("search", new UserDto());
            return "home/teachers";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
}
