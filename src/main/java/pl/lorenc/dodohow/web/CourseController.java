package pl.lorenc.dodohow.web;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lorenc.dodohow.dtos.ExerciseDto;
import pl.lorenc.dodohow.dtos.ExerciseRefferenceDto;
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

                    Optional<Exercise> firstExercise = exerciseService.findAllBy(id)
                            .stream()
                            .min(Comparator.comparing(Exercise::getNumber));

                    return firstExercise.map(e -> getExerciseTemplate(model, e))
                            .orElse("redirect:/sections");
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


            //find exercise by id
            Optional<Exercise> exerciseOpt = exerciseService.findById(exercise.getId());
            if (exerciseOpt.isPresent()) {

                //find context to exercise which is user and section
                Exercise currentExercise = exerciseOpt.get();
                Optional<User> userOpt = userService.getUserFromSession();
                Optional<Section> sectionOpt = sectionService.findById(currentExercise.getSection().getId());
                if (userOpt.isPresent() && sectionOpt.isPresent()) {

                    //Check if user already submitted answer to question during this quiz
                    User user = userOpt.get();
                    Optional<Points> oldPointsOpt = pointsService.findByUserAndExercise(user, currentExercise);
                    if (oldPointsOpt.isPresent()) {
                        //action if user answered question
                        Points oldPoints = oldPointsOpt.get();
                        Optional<Score> scoreOpt = sectionService.findScore(user, sectionOpt.get());
                        if (exercise.getUserAnswer() != null && exercise.getUserAnswer().equals(currentExercise.getAnswer())) {
                            scoreOpt.ifPresent(score -> {
                                if (oldPoints.getUserScore() == 0) { //add points if previously user answered incorrectly
                                    score.setScore(score.getScore() + currentExercise.getMaxScore());
                                    sectionService.saveScore(score);
                                }
                            });
                            oldPoints.setUserScore(currentExercise.getMaxScore());
                        } else {
                            scoreOpt.ifPresent(score -> {
                                if (oldPoints.getUserScore() > 0) { //substract if previously user answered correctly
                                    score.setScore(score.getScore() > currentExercise.getMaxScore() ? score.getScore() - currentExercise.getMaxScore() : 0);
                                    sectionService.saveScore(score);
                                }
                            });
                            oldPoints.setUserScore(0);
                        }
                        pointsService.save(oldPoints);
                    } else {

                        //action if user answered this question for the first time
                        Points points = new Points(null, 0, currentExercise.getMaxScore(), user, currentExercise);
                        if (exercise.getUserAnswer() != null && exercise.getUserAnswer().equals(currentExercise.getAnswer())) {
                            points.setUserScore(currentExercise.getMaxScore());
                            Optional<Score> scoreOpt = sectionService.findScore(user, sectionOpt.get());
                            scoreOpt.ifPresent(score -> {
                                score.setScore(score.getScore() + currentExercise.getMaxScore());
                                sectionService.saveScore(score);
                            });
                        }
                        pointsService.save(points);
                    }
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
                Optional<Section> sectionOpt = sectionService.findById(currentExercise.getSection().getId());
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
        model.addAttribute("exercise", nextExercise);

        boolean previous = exerciseService.existsBySectionIdAndNumber(exercise.getSection().getId(), exercise.getNumber() - 1);
        boolean next = exerciseService.existsBySectionIdAndNumber(exercise.getSection().getId(), exercise.getNumber() + 1);

        List<ExerciseRefferenceDto> booleans = Lists.newArrayList(
                new ExerciseRefferenceDto(previous, exercise.getId()),
                new ExerciseRefferenceDto(next, exercise.getId())
        );

        model.addAttribute("booleans", booleans);

        return "exercises/" + nextExercise.getType();
    }

    private String summary(Model model, Section section) {

        Optional<User> user = userService.getUserFromSession();
        if (user.isPresent()) {

            List<PointsDto> points = getUserPoints(user.get(), section.getId());

            model.addAttribute("points", points);
            model.addAttribute("section", mapper.map(section));

            return "sections/summary";
        }
        return "redirect:/sections";
    }

    private List<PointsDto> getUserPoints(User user, Long sectionId) {
        List<Exercise> exercises = exerciseService.findAllBy(sectionId);
        List<PointsDto> points = pointsService.findAllByExercisesAndUser(exercises, user).stream()
                .map(mapper::map)
                .collect(Collectors.toList());

        List<PointsDto> missingPoints = exercises.stream()
                .filter(e -> !points.stream().map(PointsDto::getExerciseId).collect(Collectors.toList()).contains(e.getId()))
                .map(e -> new PointsDto(null, e.getId(), user.getId(), 0, e.getMaxScore(), e.getQuestion()))
                .collect(Collectors.toList());

        points.addAll(missingPoints);
        points.sort(Comparator.comparing(PointsDto::getExerciseId));

        return points;
    }
}
