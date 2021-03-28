package pl.lorenc.dodohow.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lorenc.dodohow.dtos.*;
import pl.lorenc.dodohow.entities.*;
import pl.lorenc.dodohow.repositories.ExerciseRepository;
import pl.lorenc.dodohow.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DtoMapper {

    private ExerciseRepository exerciseRepository;
    private QuizService quizService;
    private UserRepository userRepository;
    private ScoreService scoreService;


    @Autowired
    public DtoMapper(ExerciseRepository exerciseRepository, QuizService quizService, UserRepository userRepository, ScoreService scoreService) {
        this.exerciseRepository = exerciseRepository;
        this.quizService = quizService;
        this.userRepository = userRepository;
        this.scoreService = scoreService;
    }

    public User map(UserDto userDto) {
        User user = new User(userDto.getId(), userDto.getLogin(), userDto.getPassword(), userDto.getEmail(), userDto.getActive(), userDto.getRoles());

        Set<QuizDto> quizzes = userDto.getQuizList();
        if (quizzes != null && !quizzes.isEmpty()) {
            Set<Long> ids = userDto.getQuizList().stream().map(QuizDto::getId).collect(Collectors.toSet());
            user.setQuizzes(quizService.findAllByIds(ids));
        }
        return user;
    }

    public UserDto map(User user) {

        boolean teacher = user.getRoleList()
                .contains("ROLE_TEACHER");

        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getActive(), user.getRoles(), teacher);
        Set<Quiz> quizzes = quizService.findUsersQuizzes(user);
        if (quizzes != null) {
            userDto.setQuizList(quizzes.stream()
                    .map(this::map)
                    .collect(Collectors.toSet()));
        }
        return userDto;
    }

    public Quiz map(QuizDto quizDto) {
        if (quizDto.getExercises() == null || quizDto.getExercises().isEmpty()) {
            log.error("Quiz: " + quizDto.getTitle() + " with id: " + quizDto.getId() + " has no exercises");
        } else {
            Set<Exercise> exercises = exerciseRepository.findAllByIdIn(quizDto.getExercises().stream().map(ExerciseDto::getId).collect(Collectors.toSet()));
            if (exercises == null || exercises.isEmpty()) {
                log.error("Quiz: " + quizDto.getTitle() + " with id: " + quizDto.getId() + " has wrong list of exercises");
            } else {
                return new Quiz(quizDto.getId(), quizDto.getTitle(), exercises, quizDto.getMaxScore(), quizDto.getNumberInClass());
            }
        }
        return null;
    }

    public QuizDto map(Quiz quiz) {
        Set<ExerciseDto> exercises = quiz.getExercises().stream()
                .map(this::map)
                .collect(Collectors.toSet());
        return new QuizDto(quiz.getId(), quiz.getTitle(), exercises, quiz.getMaxScore(), quiz.getNumberInClass());
    }

    public QuizWithScoreDto map(Quiz quiz, User user) {
        List<Score> scores = scoreService.getScores(user, quiz);
        if (scores != null && !scores.isEmpty()) {
            Optional<Score> scoreOpt = scores.stream().max(Comparator.comparing(Score::getScore));
            return scoreOpt.map(s -> new QuizWithScoreDto(quiz.getId(), quiz.getTitle(), quiz.getMaxScore(), s.getScore()))
                    .orElse(new QuizWithScoreDto(quiz.getId(), quiz.getTitle(), quiz.getMaxScore(), 0));
        }
        return new QuizWithScoreDto(quiz.getId(), quiz.getTitle(), quiz.getMaxScore(), 0);
    }

    public Exercise map(ExerciseDto exerciseDto) {
        Optional<ExerciseType> exerciseTypeOptional = ExerciseType.findByLabel(exerciseDto.getType());
        if (exerciseTypeOptional.isPresent()) {
            Optional<Quiz> quizOptional = quizService.findById(exerciseDto.getQuizId());
            if (quizOptional.isPresent()) {
                return new Exercise(exerciseDto.getId(), exerciseDto.getMaxScore(), exerciseDto.getQuestion(), exerciseDto.getAnswer(), exerciseDto.getWrongAnswers(), quizOptional.get(), exerciseTypeOptional.get(), exerciseDto.getNumber());
            } else {
                log.error("Quiz with id: " + exerciseDto.getQuizId() + " doesn't exist!");
            }
        } else {
            log.error("Exercise type: " + exerciseDto.getType() + " doesn't exist!");
        }
        return null;
    }

    public ExerciseDto map(Exercise exercise) {
        return new ExerciseDto(exercise.getId(), exercise.getMaxScore(), exercise.getQuestion(), exercise.getAnswer(), exercise.getWrongAnswers(), exercise.getQuiz().getId(), exercise.getType().getLabel(), exercise.getNumber());
    }

    public Score map(ScoreDto scoreDto) {
        Optional<User> user = userRepository.findById(scoreDto.getUserId());
        if (user.isPresent()) {
            Optional<Quiz> quiz = quizService.findById(scoreDto.getQuizId());
            if (quiz.isPresent()) {
                return new Score(scoreDto.getId(), user.get(), quiz.get(), scoreDto.getScore());
            } else {
                log.error("Quiz with id: " + scoreDto.getQuizId() + " doesn't exist!");
            }
        } else {
            log.error("User with id: " + scoreDto.getUserId() + " doesn't exist!");
        }
        return null;
    }

    public ScoreDto map(Score score) {
        return new ScoreDto(score.getId(), score.getUser().getId(), score.getQuiz().getId(), score.getScore());
    }

    public UserQuizDto mapToQuiz(User user, Long quizId) {
        return quizService.findById(quizId)
                .map(quiz -> new UserQuizDto(user.getId(), map(quiz)))
                .orElse(null);
    }

    public Points map(PointsDto pointsDto) {
        Optional<User> user = userRepository.findById(pointsDto.getUserId());
        if (user.isPresent()) {
            Optional<Exercise> exercise = exerciseRepository.findById(pointsDto.getExerciseId());
            if (exercise.isPresent()) {
                return new Points(pointsDto.getId(), pointsDto.getUserScore(), pointsDto.getMaxScore(), user.get(), exercise.get());
            } else {
                log.error("Exercise with id: " + pointsDto.getId() + " doesn't exist!");
            }
        } else {
            log.error("User with id: " + pointsDto.getId() + " doesn't exist!");
        }
        return null;
    }

    public PointsDto map(Points points) {

        Optional<Exercise> exerciseOpt = exerciseRepository.findById(points.getExercise().getId());
        return exerciseOpt.map(exercise -> new PointsDto(points.getId(), exercise.getId(), exercise.getNumber(), points.getUser().getId(), points.getUserScore(), points.getMaxScore(), points.getExercise().getQuestion()))
                .orElse(null);
    }

    public QuizClassDto map(QuizClass quizClass) {

        if (quizClass == null)
            return null;

        Set<Long> students = new HashSet<>();
        if (quizClass.getStudents() != null) {
            students = quizClass.getStudents()
                    .stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());
        }

        Set<Long> quizzes = new HashSet<>();
        if (quizClass.getQuizzes() != null) {
            quizzes = quizClass.getQuizzes()
                    .stream()
                    .map(Quiz::getId)
                    .collect(Collectors.toSet());
        }

        return new QuizClassDto(quizClass.getId(), quizClass.getTeacher().getId(), students, quizzes);
    }

    public QuizClass map(QuizClassDto quizClassDto) {

        if (quizClassDto == null)
            return null;

        Optional<User> teacherOpt = userRepository.findById(quizClassDto.getTeacherId());
        Set<User> students = userRepository.findAllByIdIn(quizClassDto.getStudents());
        Set<Quiz> quizzes = quizService.findAllByIds(quizClassDto.getQuizList());

        if (teacherOpt.isEmpty())
            log.error("User with id: " + quizClassDto.getTeacherId() + " doesn't exist!");

        return new QuizClass(quizClassDto.getId(), teacherOpt.orElse(null), students, quizzes);

    }

}
