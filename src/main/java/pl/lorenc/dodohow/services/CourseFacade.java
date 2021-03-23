package pl.lorenc.dodohow.services;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lorenc.dodohow.dtos.ExerciseDto;
import pl.lorenc.dodohow.dtos.ExerciseRefferenceDto;
import pl.lorenc.dodohow.dtos.PointsDto;
import pl.lorenc.dodohow.dtos.ScoreDto;
import pl.lorenc.dodohow.entities.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseFacade {

    private UserService userService;
    private SectionService sectionService;
    private ExerciseService exerciseService;
    private PointsService pointsService;
    private ScoreService scoreService;
    private DtoMapper mapper;

    public CourseFacade(UserService userService, SectionService sectionService, ExerciseService exerciseService, PointsService pointsService, ScoreService scoreService, DtoMapper mapper) {
        this.userService = userService;
        this.sectionService = sectionService;
        this.exerciseService = exerciseService;
        this.pointsService = pointsService;
        this.scoreService = scoreService;
        this.mapper = mapper;
    }

    public String newCourse(Long id, Model model) {
        try {

            Optional<User> userOpt = userService.getUserFromSession();

            return userOpt.map(user -> {

                Optional<Section> sectionOpt = sectionService.findById(id);
                if (sectionOpt.isPresent()) {

                    scoreService.deleteScore(user, sectionOpt.get());
                    List<Exercise> exercises = exerciseService.findAllBy(sectionOpt.get().getId());
                    pointsService.deleteAllByUserAndExercises(user, exercises);

                    Optional<Exercise> firstExercise = exerciseService.findAllBy(id)
                            .stream()
                            .min(Comparator.comparing(Exercise::getNumber));

                    model.addAttribute("map", getUserPoints(user, sectionOpt.get().getId()));

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


    public String nextExercise(ExerciseDto exercise) {
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
                    User user = userOpt.get();

                    if (!scoreService.checkIfScoreExists(sectionOpt.get(), user)) {
                        ScoreDto scoreDto = new ScoreDto(null, user.getId(), sectionOpt.get().getId(), 0);
                        scoreService.saveScore(mapper.map(scoreDto));
                    }

                    //Check if user already submitted answer to question during this quiz
                    Optional<Points> oldPointsOpt = pointsService.findByUserAndExercise(user, currentExercise);
                    if (oldPointsOpt.isPresent()) {
                        //action if user answered question
                        Points oldPoints = oldPointsOpt.get();
                        Optional<Score> scoreOpt = scoreService.findScore(user, sectionOpt.get());
                        if (exercise.getUserAnswer() != null && exercise.getUserAnswer().equals(currentExercise.getAnswer())) {
                            scoreOpt.ifPresent(score -> {
                                if (oldPoints.getUserScore() == 0) { //add points if previously user answered incorrectly
                                    score.setScore(score.getScore() + currentExercise.getMaxScore());
                                    scoreService.saveScore(score);
                                }
                            });
                            oldPoints.setUserScore(currentExercise.getMaxScore());
                        } else {
                            scoreOpt.ifPresent(score -> {
                                if (oldPoints.getUserScore() > 0) { //substract if previously user answered correctly
                                    score.setScore(score.getScore() > currentExercise.getMaxScore() ? score.getScore() - currentExercise.getMaxScore() : 0);
                                    scoreService.saveScore(score);
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
                            Optional<Score> scoreOpt = scoreService.findScore(user, sectionOpt.get());
                            scoreOpt.ifPresent(score -> {
                                score.setScore(score.getScore() + currentExercise.getMaxScore());
                                scoreService.saveScore(score);
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

    public String getExercise(Long id, Model model) {
        try {
            Optional<Exercise> exerciseOpt = exerciseService.findById(id);

            if (exerciseOpt.isEmpty()) {
                return "redirect:/sections";
            }

            userService.getUserFromSession()
                    .ifPresent(u -> model.addAttribute("map", getUserPoints(u, exerciseOpt.get().getSection().getId())));

            return getExerciseTemplate(model, exerciseOpt.get());

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/sections";
        }
    }


    public String getNextExercise(Long id, Model model) {
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

            userService.getUserFromSession()
                    .ifPresent(u -> model.addAttribute("map", getUserPoints(u, next.get().getSection().getId())));

            return getExerciseTemplate(model, next.get());
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/sections";
        }
    }

    public String getPreviousExercise(Long id, Model model) {
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

            userService.getUserFromSession()
                    .ifPresent(u -> model.addAttribute("map", getUserPoints(u, previous.get().getSection().getId())));

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
                .map(p -> {
                    PointsDto pDto = mapper.map(p);
                    pDto.setSkipped(false);
                    return pDto;
                })
                .collect(Collectors.toList());

        List<PointsDto> missingPoints = exercises.stream()
                .filter(e -> !points.stream().map(PointsDto::getExerciseId).collect(Collectors.toList()).contains(e.getId()))
                .map(e -> new PointsDto(null, e.getId(), e.getNumber(), user.getId(), 0, e.getMaxScore(), e.getQuestion(), true))
                .collect(Collectors.toList());

        points.addAll(missingPoints);
        points.sort(Comparator.comparing(PointsDto::getExerciseNumber));

        return points;
    }

}
