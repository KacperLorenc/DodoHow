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
    private QuizService quizService;
    private ExerciseService exerciseService;
    private PointsService pointsService;
    private ScoreService scoreService;
    private DtoMapper mapper;

    public CourseFacade(UserService userService, QuizService quizService, ExerciseService exerciseService, PointsService pointsService, ScoreService scoreService, DtoMapper mapper) {
        this.userService = userService;
        this.quizService = quizService;
        this.exerciseService = exerciseService;
        this.pointsService = pointsService;
        this.scoreService = scoreService;
        this.mapper = mapper;
    }

    public String newQuiz(Long id, Model model) {
        try {

            Optional<User> userOpt = userService.getUserFromSession();

            return userOpt.map(user -> {
                ScoreDto scoreDto = new ScoreDto(null, user.getId(), id, 0);
                Optional<Quiz> quizOpt = quizService.findById(id);
                if (quizOpt.isPresent()) {

                    scoreService.deleteScore(user, quizOpt.get());
                    scoreService.saveScore(mapper.map(scoreDto));

                    List<Exercise> exercises = exerciseService.findAllBy(quizOpt.get().getId());
                    pointsService.deleteAllByUserAndExercises(user, exercises);

                    Optional<Exercise> firstExercise = exerciseService.findAllBy(id)
                            .stream()
                            .min(Comparator.comparing(Exercise::getNumber));

                    model.addAttribute("map", getUserPoints(user, quizOpt.get().getId()));

                    return firstExercise.map(e -> getExerciseTemplate(model, e))
                            .orElse("redirect:/quizzes");
                }

                return "redirect:/quizzes";
            }).orElse("redirect:/quizzes");


        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/quizzes";
        }
    }


    public String nextExercise(ExerciseDto exercise) {
        try {

            if (exercise.getId() == null) {
                return "redirect:/quizzes";
            }

            //find exercise by id
            Optional<Exercise> exerciseOpt = exerciseService.findById(exercise.getId());
            if (exerciseOpt.isPresent()) {

                //find context to exercise which is user and quiz
                Exercise currentExercise = exerciseOpt.get();
                Optional<User> userOpt = userService.getUserFromSession();
                Optional<Quiz> quizOpt = quizService.findById(currentExercise.getQuiz().getId());
                if (userOpt.isPresent() && quizOpt.isPresent()) {

                    //Check if user already submitted answer to question during this quiz
                    User user = userOpt.get();
                    Optional<Points> oldPointsOpt = pointsService.findByUserAndExercise(user, currentExercise);
                    if (oldPointsOpt.isPresent()) {
                        //action if user answered question
                        Points oldPoints = oldPointsOpt.get();
                        Optional<Score> scoreOpt = scoreService.findScore(user, quizOpt.get());
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
                            Optional<Score> scoreOpt = scoreService.findScore(user, quizOpt.get());
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
            return "redirect:/quizzes";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/quizzes";
        }
    }

    public String getExercise(Long id, Model model) {
        try {
            Optional<Exercise> exerciseOpt = exerciseService.findById(id);

            if (exerciseOpt.isEmpty()) {
                return "redirect:/quizzes";
            }

            userService.getUserFromSession()
                    .ifPresent(u -> model.addAttribute("map", getUserPoints(u, exerciseOpt.get().getQuiz().getId())));

            return getExerciseTemplate(model, exerciseOpt.get());

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/quizzes";
        }
    }


    public String getNextExercise(Long id, Model model) {
        try {
            Optional<Exercise> exerciseOpt = exerciseService.findById(id);

            if (exerciseOpt.isEmpty()) {
                return "redirect:/quizzes";
            }

            Exercise currentExercise = exerciseOpt.get();
            Optional<Exercise> next = exerciseService.findByQuizIdAndNumber(currentExercise.getQuiz().getId(), currentExercise.getNumber() + 1);
            if (next.isEmpty()) {
                Optional<Quiz> quizOpt = quizService.findById(currentExercise.getQuiz().getId());
                return quizOpt.map(quiz -> summary(model, quiz)).orElse("redirect:/quizzes");
            }

            userService.getUserFromSession()
                    .ifPresent(u -> model.addAttribute("map", getUserPoints(u, next.get().getQuiz().getId())));

            return getExerciseTemplate(model, next.get());
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/quizzes";
        }
    }

    public String getPreviousExercise(Long id, Model model) {
        try {
            Optional<Exercise> exerciseOpt = exerciseService.findById(id);

            if (exerciseOpt.isEmpty()) {
                return "redirect:/quizzes";
            }

            Exercise currentExercise = exerciseOpt.get();

            Optional<Exercise> previous = exerciseService.findByQuizIdAndNumber(currentExercise.getQuiz().getId(), currentExercise.getNumber() - 1);

            if (previous.isEmpty()) {
                Optional<Quiz> quizOpt = quizService.findById(currentExercise.getId());
                return quizOpt.map(quiz -> summary(model, quiz)).orElse("redirect:/quizzes");
            }

            userService.getUserFromSession()
                    .ifPresent(u -> model.addAttribute("map", getUserPoints(u, previous.get().getQuiz().getId())));

            return getExerciseTemplate(model, previous.get());

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/quizzes";
        }
    }

    private String getExerciseTemplate(Model model, Exercise exercise) {

        ExerciseDto nextExercise = mapper.map(exercise);
        model.addAttribute("exercise", nextExercise);

        boolean previous = exerciseService.existsByQuizIdAndNumber(exercise.getQuiz().getId(), exercise.getNumber() - 1);
        boolean next = exerciseService.existsByQuizIdAndNumber(exercise.getQuiz().getId(), exercise.getNumber() + 1);

        List<ExerciseRefferenceDto> booleans = Lists.newArrayList(
                new ExerciseRefferenceDto(previous, exercise.getId()),
                new ExerciseRefferenceDto(next, exercise.getId())
        );

        model.addAttribute("booleans", booleans);

        return "exercises/" + nextExercise.getType();
    }

    private String summary(Model model, Quiz quiz) {

        Optional<User> user = userService.getUserFromSession();
        if (user.isPresent()) {

            List<PointsDto> points = getUserPoints(user.get(), quiz.getId());

            model.addAttribute("points", points);
            model.addAttribute("quiz", mapper.map(quiz));

            return "quizzes/summary";
        }
        return "redirect:/quizzes";
    }

    private List<PointsDto> getUserPoints(User user, Long quizId) {
        List<Exercise> exercises = exerciseService.findAllBy(quizId);
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
