package pl.olafszewczak.dodohow.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.olafszewczak.dodohow.dtos.ExerciseDto;
import pl.olafszewczak.dodohow.dtos.ScoreDto;
import pl.olafszewczak.dodohow.entities.Exercise;
import pl.olafszewczak.dodohow.entities.Score;
import pl.olafszewczak.dodohow.entities.Section;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.services.DtoMapper;
import pl.olafszewczak.dodohow.services.ExerciseService;
import pl.olafszewczak.dodohow.services.SectionService;
import pl.olafszewczak.dodohow.services.UserService;

import java.util.Comparator;
import java.util.Optional;

@Controller
public class CourseController {
    private UserService userService;
    private SectionService sectionService;
    private ExerciseService exerciseService;
    private DtoMapper mapper;

    public CourseController(UserService userService, SectionService sectionService, ExerciseService exerciseService, DtoMapper mapper) {
        this.userService = userService;
        this.sectionService = sectionService;
        this.exerciseService = exerciseService;
        this.mapper = mapper;
    }

    @GetMapping("/course/{id}")
    public String newCourse(@PathVariable Long id, Model model) {
        try {

            Optional<User> userOpt = userService.getUserFromSession();

            return userOpt.map(user -> {
                ScoreDto scoreDto = new ScoreDto(null, user.getId(), id, 0);
                Optional<Section> sectionOpt = sectionService.findById(id);
                if (sectionOpt.isPresent()) {

                    sectionService.deleteScore(user, sectionOpt.get());
                    sectionService.saveScore(mapper.map(scoreDto));

                    Optional<ExerciseDto> firstExercise = exerciseService.findAllBy(id).stream()
                            .map(mapper::map).min(Comparator.comparing(ExerciseDto::getNumber));

                    return firstExercise.map(e -> {
                        model.addAttribute("exercise", e);
                        return "exercises/" + e.getType();
                    }).orElse("redirect:/");
                }

                return "redirect:/";
            }).orElse("redirect:/");


        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/next-exercise")
    public String nextExercise(@ModelAttribute("exercise") ExerciseDto exercise) {
        try {

            Optional<User> userOpt = userService.getUserFromSession();
            Optional<Section> sectionOpt = sectionService.findById(exercise.getSectionId());
            if (userOpt.isPresent() && sectionOpt.isPresent()) {
                if (exercise.getUserAnswer() != null && exercise.getUserAnswer().equals(exercise.getAnswer())) {
                    Optional<Score> scoreOpt = sectionService.findScore(userOpt.get(), sectionOpt.get());
                    scoreOpt.ifPresent(score -> {
                        score.setScore(score.getScore() + exercise.getMaxScore());
                        sectionService.saveScore(score);
                    });
                }

                return "redirect:/next-exercise?id=" + exercise.getId();

            } else {
                return "redirect:/";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/next-exercise")
    public String getNextExercise(@RequestParam Long id, Model model) {
        try {
            Optional<Exercise> exerciseOpt = exerciseService.findById(id);

            if (exerciseOpt.isEmpty()) {
                return "redirect:/sections";
            }

            Optional<Exercise> nextExerciseOpt = exerciseService.findNextExercise(exerciseOpt.get().getSection().getId(), exerciseOpt.get().getNumber() + 1);

            if (nextExerciseOpt.isEmpty()) {
                return "redirect:/sections";
            }

            ExerciseDto nextExercise = mapper.map(nextExerciseOpt.get());
            Optional<User> userOpt = userService.getUserFromSession();
            Optional<Section> sectionOpt = sectionService.findById(nextExercise.getSectionId());

            if (userOpt.isPresent() && sectionOpt.isPresent()) {
                model.addAttribute("exercise", nextExercise);
                return "exercises/" + nextExercise.getType();
            } else {
                return "redirect:/";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
}
