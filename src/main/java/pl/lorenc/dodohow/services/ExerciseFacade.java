package pl.lorenc.dodohow.services;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import pl.lorenc.dodohow.dtos.*;
import pl.lorenc.dodohow.entities.*;
import pl.lorenc.dodohow.utility.ExerciseType;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExerciseFacade {

    private UserService userService;
    private QuizService quizService;
    private ExerciseService exerciseService;
    private PointsService pointsService;
    private ScoreService scoreService;
    private ClassService classService;
    private DtoMapper mapper;

    public ExerciseFacade(UserService userService, QuizService quizService, ExerciseService exerciseService, PointsService pointsService, ScoreService scoreService, ClassService classService, DtoMapper mapper) {
        this.userService = userService;
        this.quizService = quizService;
        this.exerciseService = exerciseService;
        this.pointsService = pointsService;
        this.scoreService = scoreService;
        this.classService = classService;
        this.mapper = mapper;
    }

    public String getQuizzes(@PathVariable Long id, Model model) {
        try {
            Optional<User> userOptional = userService.getUserFromSession();
            Optional<QuizClass> classOpt = classService.findById(id);

            if (classOpt.isEmpty())
                return "redirect:/";

            return userOptional.map(user -> {

                QuizClass quizClass = classOpt.get();

                List<Long> quizIds = scoreService.findAllByUser(user)
                        .stream()
                        .map(s -> s.getQuiz().getId())
                        .distinct()
                        .collect(Collectors.toList());

                Set<Quiz> userQuizzes = quizService.findAll(quizIds, quizClass, true); //znajduje quizy w danej klasie
                if (userQuizzes.isEmpty()) {
                    quizService.getFirstQuiz(id).ifPresent(userQuizzes::add); //jesli nie ma zadnych przerobionych to przypisuje pierwszy quiz w klasie
                } else {
                    quizService.findNextQuiz(userQuizzes, id).ifPresent(userQuizzes::add); //jesli ma jakis przerobiony to dodaje kolejny
                }
                userService.saveRegisteredUser(user);
                model.addAttribute("user", mapper.map(user));
                model.addAttribute("quizzes", userQuizzes.stream().sorted(Comparator.comparing(Quiz::getNumberInClass)).map(quiz -> mapper.map(quiz, user)).collect(Collectors.toList()));
                return "course/quizzes";
            }).orElse("redirect:/login");
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    public String getQuiz(Model model, @PathVariable Long id) {
        try {
            Optional<User> userOptional = userService.getUserFromSession();
            return userOptional.map(user -> {
                if (quizService.existsById(id)) {
                    model.addAttribute("quiz", mapper.mapToQuiz(user, id));
                    return "course/quiz";         //jeśli znalazło quiz i uzytkownika to zwraca dobry template
                } else {
                    return "redirect:/classes";
                }
            }).orElse("redirect:/login");

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    public String newQuiz(Long id, Model model) {
        try {

            Optional<User> userOpt = userService.getUserFromSession();

            return userOpt.map(user -> {
                ScoreDto scoreDto = new ScoreDto(null, user.getId(), id, 0, false);
                Optional<Quiz> quizOpt = quizService.findById(id);
                if (quizOpt.isPresent()) {

                    Quiz quiz = quizOpt.get();

                    if (scoreService.checkIfScoreExists(quiz, user) && (quiz.getRepeatable() == null || !quiz.getRepeatable()))
                        return "redirect:/classes";

                    scoreService.deleteScore(user, quizOpt.get());
                    scoreService.saveScore(mapper.map(scoreDto));

                    Set<Exercise> exercises = quiz.getExercises();
                    pointsService.deleteAllByUserAndExercises(user, new ArrayList<>(exercises));

                    Optional<Exercise> firstExercise = exercises
                            .stream()
                            .min(Comparator.comparing(Exercise::getNumber));

                    model.addAttribute("map", getUserPoints(user, quizOpt.get().getId()));

                    return firstExercise.map(e -> getExerciseTemplate(model, e))
                            .orElse("redirect:/classes");
                }
                return "redirect:/classes";
            }).orElse("redirect:/classes");


        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }


    public String nextExercise(ExerciseDto exercise) {
        try {

            if (exercise.getId() == null) {
                return "redirect:/classes";
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
            return "redirect:/classes";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    public String getExercise(Long exerciseId, Model model) {
        try {
            Optional<Exercise> exerciseOpt = exerciseService.findById(exerciseId);

            if (exerciseOpt.isEmpty()) {
                return "redirect:/classes";
            }
            return userService.getUserFromSession()
                    .map(u -> {
                        Long quizId = exerciseOpt.get().getQuiz().getId();
                        if (quizFinished(u, quizId))
                            return "redirect:/classes";

                        model.addAttribute("map", getUserPoints(u, exerciseOpt.get().getQuiz().getId()));
                        return getExerciseTemplate(model, exerciseOpt.get());
                    }).orElse("redirect:/classes");

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }


    public String getNextExercise(Long id) {
        try {
            Optional<Exercise> exerciseOpt = exerciseService.findById(id);

            if (exerciseOpt.isEmpty()) {
                return "redirect:/classes";
            }

            Exercise currentExercise = exerciseOpt.get();
            Optional<Exercise> next = exerciseService.findByQuizIdAndNumber(currentExercise.getQuiz().getId(), currentExercise.getNumber() + 1);
            if (next.isEmpty()) {
                return "redirect:/exercise?number=" + id;
            }

            return "redirect:/exercise?number=" + next.get().getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    public String getPreviousExercise(Long id) {
        try {
            Optional<Exercise> exerciseOpt = exerciseService.findById(id);

            if (exerciseOpt.isEmpty()) {
                return "redirect:/classes";
            }

            Exercise currentExercise = exerciseOpt.get();
            Optional<Exercise> previous = exerciseService.findByQuizIdAndNumber(currentExercise.getQuiz().getId(), currentExercise.getNumber() - 1);

            if (previous.isEmpty()) {
                return "redirect:/exercise?number=" + id;
            }

            return "redirect:/exercise?number=" + previous.get().getId();

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
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
        model.addAttribute("quizId", exercise.getQuiz().getId());
        model.addAttribute("booleans", booleans);

        return "exercises/" + exercise.getType().getName();
    }

    public String redirectSummary(Long quizId) {
        Optional<Quiz> quizOpt = quizService.findById(quizId);
        if (quizOpt.isPresent()) {
            Quiz quiz = quizOpt.get();
            userService.getUserFromSession().flatMap(u -> scoreService.findScore(u, quiz))
                    .ifPresent(s -> {
                        s.setQuizFinished(true);
                        scoreService.saveScore(s);
                    });
            return "redirect:/summary?quiz=" + quizId;
        }
        return "redirect:/classes";
    }

    public String summary(Model model, Long quizId) {
        Optional<User> user = userService.getUserFromSession();
        if (user.isPresent()) {
            Optional<Quiz> quizOpt = quizService.findById(quizId);
            if (quizOpt.isPresent()) {
                List<PointsDto> points = getUserPoints(user.get(), quizId);
                model.addAttribute("points", points);
                model.addAttribute("quiz", mapper.map(quizOpt.get()));

                return "course/summary";
            }
        }
        return "redirect:/classes";
    }

    public String newExercise(String exerciseType, Long quizId, Model model) {
        Optional<Quiz> quizOpt = quizService.findById(quizId);
        if (quizOpt.isEmpty())
            return "redirect:/classes";
        if (!ExerciseType.getAllNames().contains(exerciseType))
            return "redirect:/classes";
        Quiz quiz = quizOpt.get();
        if (authenticateUser(quiz.getQuizClass().getId())) {
            ExerciseDto exercise = new ExerciseDto();
            ExerciseType.findByName(exerciseType).ifPresent(t -> {
                model.addAttribute("type", t.getLabel());
                if (t.equals(ExerciseType.TRUTH_FALSE))
                    exercise.setWrongAnswers("true;false");
            });
            model.addAttribute("quizId", quizId);
            model.addAttribute("exercise", exercise);

            return "exercises/" + exerciseType + "form";
        }
        return "redirect:/classes";
    }

    public String chooseExerciseType(ExerciseTypeDto exerciseType) {
        Optional<Quiz> quizOpt = quizService.findById(exerciseType.getQuizId());
        if (quizOpt.isEmpty())
            return "redirect:/classes";
        Quiz quiz = quizOpt.get();
        if (authenticateUser(quiz.getQuizClass().getId())) {

            Optional<ExerciseType> typeOpt = ExerciseType.findByLabel(exerciseType.getType());
            return typeOpt
                    .map(type -> "redirect:/exercises/new-exercise?type=" + type.getName() + "&quiz=" + exerciseType.getQuizId())
                    .orElse("redirect:/classes");
        }
        return "redirect:/classes";
    }

    public String updateExercise(Long exerciseId, Model model) {
        Optional<Exercise> exerciseOpt = exerciseService.findById(exerciseId);
        if (exerciseOpt.isEmpty())
            return "redirect:/classes";
        Exercise exercise = exerciseOpt.get();
        Optional<Quiz> quizOpt = quizService.findById(exercise.getQuiz().getId());
        if (quizOpt.isEmpty())
            return "redirect:/classes";
        Quiz quiz = quizOpt.get();
        if (!authenticateUser(quiz.getQuizClass().getId()))
            return "redirect:/classes";
        model.addAttribute("exercise", mapper.map(exercise));
        model.addAttribute("quizId", quiz.getId());

        return "exercises/" + exercise.getType().getName() + "form";
    }

    public String chooseType(Long quizId, Model model) {
        Optional<Quiz> quizOpt = quizService.findById(quizId);
        if (quizOpt.isEmpty())
            return "redirect:/classes";
        Quiz quiz = quizOpt.get();
        if (authenticateUser(quiz.getQuizClass().getId())) {
            ExerciseTypeDto exerciseType = new ExerciseTypeDto();
            exerciseType.setQuizId(quizId);
            model.addAttribute("typeobject", exerciseType);
            model.addAttribute("quizId", quizId);
            return "exercises/chooseexercisetype";
        }
        return "redirect:/classes";
    }

    public String addExerciseTranslateWord(ExerciseDto exerciseDto, Long quizId) {
        Optional<Quiz> quizOpt = quizService.findById(quizId);
        if (quizOpt.isEmpty())
            return "redirect:/classes";
        Exercise exercise = mapper.map(exerciseDto);
        exercise.setType(ExerciseType.TRANSLATE_WORD);

        return addExercise(exercise, quizOpt.get());
    }

    public String addExerciseTypeIn(ExerciseDto exerciseDto, Long quizId) {
        Optional<Quiz> quizOpt = quizService.findById(quizId);
        if (quizOpt.isEmpty())
            return "redirect:/classes";
        Exercise exercise = mapper.map(exerciseDto);
        exercise.setType(ExerciseType.TYPE_SENTENCE);

        return addExercise(exercise, quizOpt.get());
    }

    public String addExerciseFillBlank(ExerciseDto exerciseDto, Long quizId) {
        Optional<Quiz> quizOpt = quizService.findById(quizId);
        if (quizOpt.isEmpty())
            return "redirect:/classes";
        Exercise exercise = mapper.map(exerciseDto);
        exercise.setType(ExerciseType.FILL_THE_BLANK);

        return addExercise(exercise, quizOpt.get());
    }

    public String addExerciseChooseAnswer(ExerciseDto exerciseDto, Long quizId) {
        Optional<Quiz> quizOpt = quizService.findById(quizId);
        if (quizOpt.isEmpty())
            return "redirect:/classes";
        Exercise exercise = mapper.map(exerciseDto);
        exercise.setType(ExerciseType.CHOOSE_ANSWER);

        return addExercise(exercise, quizOpt.get());
    }

    public String addExerciseTrueFalse(ExerciseDto exerciseDto, Long quizId) {
        Optional<Quiz> quizOpt = quizService.findById(quizId);
        if (quizOpt.isEmpty())
            return "redirect:/classes";
        Exercise exercise = mapper.map(exerciseDto);
        exercise.setType(ExerciseType.TRUTH_FALSE);
        exercise.setWrongAnswers("true;false");

        return addExercise(exercise, quizOpt.get());
    }

    public String deleteExercise(Long exerciseId) {
        Optional<Exercise> exerciseOpt = exerciseService.findById(exerciseId);
        if (exerciseOpt.isEmpty())
            return "redirect:/classes";
        Exercise exercise = exerciseOpt.get();
        Optional<Quiz> quizOpt = quizService.findById(exercise.getQuiz().getId());
        if (quizOpt.isEmpty())
            return "redirect:/classes";
        Quiz quiz = quizOpt.get();
        if (!authenticateUser(quiz.getQuizClass().getId()))
            return "redirect:/classes";

        List<Exercise> exercises = quiz.getExercises().stream()
                .sorted(Comparator.comparing(Exercise::getNumber))
                .collect(Collectors.toList());

        quiz.getExercises().stream()
                .filter(e -> e.getId().equals(exercise.getId()))
                .findFirst()
                .ifPresent(e -> {
                    quiz.getExercises().remove(e);
                    exercises.remove(e);
                });

        exercises.forEach(e -> e.setNumber(exercises.indexOf(e) + 1));

        exercise.setQuiz(null);
        quiz.setMaxScore(quiz.getMaxScore() - exercise.getMaxScore());
        exerciseService.save(exercise);
        quizService.save(quiz);
        return "redirect:/classes/exercises?quiz=" + quiz.getId();
    }

    private String addExercise(Exercise exercise, Quiz quiz) {

        if (!authenticateUser(quiz.getQuizClass().getId()))
            return "redirect:/classes";

        Set<Exercise> quizExercises = quiz.getExercises();

        if (exercise.getId() != null) {
            exerciseService.findById(exercise.getId()).ifPresent(e -> {
                if (!e.getType().equals(exercise.getType()))
                    exercise.setId(null);
            });
            quizExercises.stream()
                    .filter(e -> e.getId().equals(exercise.getId()))
                    .findFirst()
                    .ifPresent(quizExercises::remove);
        }

        if (exercise.getNumber() == null) {
            if (quizExercises == null || quizExercises.isEmpty()) {
                quiz.setExercises(new HashSet<>());
                exercise.setNumber(1);
            } else {
                int number = quizExercises.stream()
                        .map(Exercise::getNumber)
                        .max(Integer::compareTo)
                        .orElse(0);
                exercise.setNumber(number + 1);
            }
        } else if (quizExercises.stream().anyMatch(e -> e.getNumber().equals(exercise.getNumber()))) {
            int number = quizExercises.stream()
                    .map(Exercise::getNumber)
                    .max(Integer::compareTo)
                    .orElse(0);
            exercise.setNumber(number + 1);
        }

        if (quiz.getMaxScore() == null)
            quiz.setMaxScore(0);

        String answers = exercise.getWrongAnswers();
        if (answers != null && !answers.contains(exercise.getAnswer())) {
            StringBuilder builder = new StringBuilder();
            builder.append(answers);
            if (answers.charAt(answers.length() - 1) != ';')
                builder.append(";");
            builder.append(exercise.getAnswer());
            exercise.setWrongAnswers(builder.toString());
        } else {
            exercise.setWrongAnswers("");
        }

        if(exercise.getId() != null) {
            Optional<Exercise> exOpt = quiz.getExercises().stream().filter(e -> e.getId().equals(exercise.getId())).findFirst();
            if(exOpt.isPresent()) {
                quiz.setMaxScore(quiz.getMaxScore() + Math.abs(exercise.getMaxScore() - exOpt.get().getMaxScore()));
            } else {
                quiz.setMaxScore(quiz.getMaxScore() + exercise.getMaxScore());
            }
        } else {
            quiz.setMaxScore(quiz.getMaxScore() + exercise.getMaxScore());
        }

        quiz.getExercises().add(exercise);
        exercise.setQuiz(quiz);
        quizService.save(quiz);

        return "redirect:/classes/exercises?quiz=" + quiz.getId();
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

    private boolean quizFinished(User user, Long quizId) {
        Optional<Quiz> quizOpt = quizService.findById(quizId);
        if (quizOpt.isEmpty())
            return true;

        return quizOpt.map(quiz ->
                scoreService.findScore(user, quiz)
                        .map(score -> score.getQuizFinished() != null && score.getQuizFinished())
                        .orElse(true)
        ).orElse(true);
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
