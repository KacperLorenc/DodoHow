package pl.lorenc.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lorenc.dodohow.dtos.QuizWithScoreDto;
import pl.lorenc.dodohow.entities.Quiz;
import pl.lorenc.dodohow.entities.QuizClass;
import pl.lorenc.dodohow.entities.User;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizFacade {

    private final QuizService quizService;
    private final ClassService classService;
    private final UserService userService;
    private final DtoMapper mapper;


    @Autowired
    public QuizFacade(QuizService quizService, ClassService classService, UserService userService, DtoMapper mapper, ScoreService scoreService) {
        this.quizService = quizService;
        this.classService = classService;
        this.userService = userService;
        this.mapper = mapper;
    }

    public String getScores(Long quizId, Model model) {
        return quizService.findById(quizId).map(quiz ->
                classService.findById(quiz.getQuizClass().getId()).map(c -> {
                    if (authenticateUser(c.getId())) {
                        List<QuizWithScoreDto> scores = userService.findAllByClass(c)
                                .stream()
                                .map(user -> mapper.map(quiz, user))
                                .sorted(Comparator.comparing(QuizWithScoreDto::getUsername))
                                .collect(Collectors.toList());
                        model.addAttribute("quiz", mapper.map(quiz));
                        model.addAttribute("classId", c.getId());
                        model.addAttribute("quizzes", scores);
                        return "teacher/quiz";
                    }
                    return "redirect:/classes";
                }).orElse("redirect:/classes")
        ).orElse("redirect:/classes");


    }

    public String activateQuiz(Long quizId) {
        Optional<Quiz> quizOpt = quizService.findById(quizId);
        if (quizOpt.isEmpty())
            return "redirect:/classes";

        Quiz quiz = quizOpt.get();

        if (!authenticateUser(quiz.getQuizClass().getId()))
            return "redirect:/classes";

        if (quiz.getActive() == null)
            quiz.setActive(true);
        else
            quiz.setActive(!quiz.getActive());

        quizService.save(quiz);

        return "redirect:/classes/quizzes?class=" + quiz.getQuizClass().getId();
    }

    private boolean authenticateUser(Long classId) {
        Optional<QuizClass> quizClass = classService.findById(classId);
        if (quizClass.isPresent()) {
            Optional<User> teacherOpt = userService.getUserFromSession();
            if (teacherOpt.isPresent()) {
                QuizClass c = quizClass.get();
                User teacher = teacherOpt.get();
                return c.getTeacherId().equals(teacher.getId());
            }
        }
        return false;
    }
}
