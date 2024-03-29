package pl.lorenc.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.lorenc.dodohow.dtos.UserListDto;
import pl.lorenc.dodohow.dtos.UserDto;
import pl.lorenc.dodohow.services.DtoMapper;
import pl.lorenc.dodohow.services.QuizService;
import pl.lorenc.dodohow.services.ScoreService;
import pl.lorenc.dodohow.services.UserService;

import java.util.Comparator;
import java.util.List;
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

    @GetMapping("/teachers")
    public String getTeachers(Model model) {
        try {

            List<UserDto> teachers = userService.findUsersBy(false, "ROLE_TEACHER")
                    .stream()
                    .map(mapper::map)
                    .sorted(Comparator.comparing(UserDto::getLogin))
                    .collect(Collectors.toList());
            model.addAttribute("teacherSet", new UserListDto(teachers));
            model.addAttribute("search", new UserDto());
            return "home/teachers";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
}
