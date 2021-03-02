package pl.lorenc.dodohow.web;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lorenc.dodohow.dtos.ExerciseDto;
import pl.lorenc.dodohow.dtos.PointsDto;
import pl.lorenc.dodohow.dtos.ScoreDto;
import pl.lorenc.dodohow.entities.*;
import pl.lorenc.dodohow.services.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CourseController {
    private UserService userService;
    private SectionService sectionService;
    private ExerciseService exerciseService;
    private PointsService pointsService;
    private DtoMapper mapper;

    public CourseController(UserService userService, SectionService sectionService, ExerciseService exerciseService, PointsService pointsService, DtoMapper mapper) {
        this.userService = userService;
        this.sectionService = sectionService;
        this.exerciseService = exerciseService;
        this.pointsService = pointsService;
        this.mapper = mapper;
    }

    @GetMapping("/course/{id}")
    @Transactional
    public String newCourse(@PathVariable Long id, Model model) {
        try {

            Optional<User> userOpt = userService.getUserFromSession();

            return userOpt.map(user -> {
                ScoreDto scoreDto = new ScoreDto(null, user.getId(), id, 0);
                Optional<Section> sectionOpt = sectionService.findById(id);
                if (sectionOpt.isPresent()) {

                    sectionService.deleteScore(user, sectionOpt.get());
                    sectionService.saveScore(mapper.map(scoreDto));

                    List<Exercise> exercises = exerciseService.findAllBy(sectionOpt.get().getId());
                    pointsService.deleteAllByUserAndExercises(user, exercises);

                    Optional<ExerciseDto> firstExercise = exerciseService.findAllBy(id).stream()
                            .map(mapper::map).min(Comparator.comparing(ExerciseDto::getNumber));

                    return firstExercise.map(e -> {
                        model.addAttribute("exercise", e);
                        return "exercises/" + e.getType();
                    }).orElse("redirect:/sections");
                }

                return "redirect:/sections";
            }).orElse("redirect:/sections");


        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/sections";
        }
    }

    @PostMapping("/next-exercise")
    @Transactional
    public String nextExercise(@ModelAttribute("exercise") ExerciseDto exercise) {
        try {

            if (exercise.getId() == null) {
                return "redirect:/sections";
            }
            Optional<Exercise> exerciseOpt = exerciseService.findById(exercise.getId());
            if (exerciseOpt.isPresent()) {
                Optional<User> userOpt = userService.getUserFromSession();
                Optional<Section> sectionOpt = sectionService.findById(exerciseOpt.get().getSection().getId());
                if (userOpt.isPresent() && sectionOpt.isPresent()) {
                    Points points = new Points(null, 0, exerciseOpt.get().getMaxScore(), userOpt.get(), exerciseOpt.get());
                    if (exercise.getUserAnswer() != null && exercise.getUserAnswer().equals(exerciseOpt.get().getAnswer())) {
                        points.setUserScore(exerciseOpt.get().getMaxScore());
                        Optional<Score> scoreOpt = sectionService.findScore(userOpt.get(), sectionOpt.get());
                        scoreOpt.ifPresent(score -> {
                            score.setScore(score.getScore() + exerciseOpt.get().getMaxScore());
                            sectionService.saveScore(score);
                        });
                    }
                    pointsService.save(points);
                    return "redirect:/next-exercise?id=" + exercise.getId();
                }
            }
            return "redirect:/sections";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/sections";
        }
    }

    @GetMapping("/next-exercise")
    @Transactional
    public String getNextExercise(@RequestParam Long id, Model model) {
        try {
            Optional<Exercise> exerciseOpt = exerciseService.findById(id);

            if (exerciseOpt.isEmpty()) {
                return "redirect:/sections";
            }

            Exercise currentExercise = exerciseOpt.get();

            Optional<Exercise> next = exerciseService.findBySectionAndNumber(currentExercise.getSection().getId(), currentExercise.getNumber() + 1);
            if (next.isEmpty()) {
                Optional<Section> sectionOpt = sectionService.findById(currentExercise.getId());
                return sectionOpt.map(section -> summary(model, section)).orElse("redirect:/sections");
            }


            return getExerciseTemplate(model, next.get());
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/sections";
        }
    }

    @GetMapping("/previous-exercise")
    @Transactional
    public String getPreviousExercise(@RequestParam Long id, Model model) {
        try {
            Optional<Exercise> exerciseOpt = exerciseService.findById(id);

            if (exerciseOpt.isEmpty()) {
                return "redirect:/sections";
            }

            Exercise currentExercise = exerciseOpt.get();

            Optional<Exercise> previous = exerciseService.findBySectionAndNumber(currentExercise.getSection().getId(), currentExercise.getNumber() - 1);

            if (previous.isEmpty()) {
                Optional<Section> sectionOpt = sectionService.findById(currentExercise.getId());
                return sectionOpt.map(section -> summary(model, section)).orElse("redirect:/sections");
            }

            return getExerciseTemplate(model, previous.get());

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/sections";
        }
    }

    private String getExerciseTemplate(Model model, Exercise exercise) {

        ExerciseDto nextExercise = mapper.map(exercise);
        Optional<Section> section = sectionService.findById(nextExercise.getSectionId());
        model.addAttribute("exercise", nextExercise);
        return "exercises/" + nextExercise.getType();
    }

    private String summary(Model model, Section section) {

        Optional<User> user = userService.getUserFromSession();
        if (user.isPresent()) {
            List<Exercise> exercises = exerciseService.findAllBy(section.getId());
            List<PointsDto> points = pointsService.findAllByExercisesAndUser(exercises, user.get()).stream()
                    .map(mapper::map)
                    .collect(Collectors.toList());

            model.addAttribute("points", points);
            model.addAttribute("section", mapper.map(section));

            return "exercises/summary";
        }
        return "redirect:/sections";
    }
}
