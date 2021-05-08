package pl.lorenc.dodohow.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lorenc.dodohow.dtos.*;
import pl.lorenc.dodohow.entities.*;
import pl.lorenc.dodohow.repositories.ClassRepository;
import pl.lorenc.dodohow.repositories.ExerciseRepository;
import pl.lorenc.dodohow.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DtoMapper {

    private final ExerciseRepository exerciseRepository;
    private final QuizService quizService;
    private final UserRepository userRepository;
    private final ScoreService scoreService;
    private final ClassRepository classRepository;

    @Autowired
    public DtoMapper(ExerciseRepository exerciseRepository, QuizService quizService, UserRepository userRepository, ScoreService scoreService, ClassRepository classRepository) {
        this.exerciseRepository = exerciseRepository;
        this.quizService = quizService;
        this.userRepository = userRepository;
        this.scoreService = scoreService;
        this.classRepository = classRepository;
    }

    public User map(UserDto userDto) {
        User user = new User(userDto.getId(), userDto.getLogin(), userDto.getPassword(), userDto.getEmail(), userDto.getActive(), userDto.getRoles());

        Set<QuizClassDto> quizzes = userDto.getClassList();
        if (quizzes != null && !quizzes.isEmpty()) {
            Set<Long> ids = userDto.getClassList().stream().map(QuizClassDto::getId).collect(Collectors.toSet());
            user.setClasses(classRepository.findAllByIdIn(ids));
        }
        return user;
    }

    public UserDto map(User user) {

        boolean teacher = user.getRoleList()
                .contains("ROLE_TEACHER");

        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getActive(), user.getRoles(), teacher);
        Set<QuizClass> quizClasses = classRepository.findAllByStudentsContaining(user);
        if (quizClasses != null) {
            userDto.setClassList(quizClasses.stream()
                    .map(this::map)
                    .collect(Collectors.toSet()));
        }
        return userDto;
    }

    public Quiz map(QuizDto quizDto) {

        Set<Exercise> exercises;

        if (quizDto.getExercises() == null || quizDto.getExercises().isEmpty()) {
            exercises = new HashSet<>();
        } else {
            exercises = exerciseRepository.findAllByIdIn(quizDto.getExercises().stream().map(ExerciseDto::getId).collect(Collectors.toSet()));
        }

        Optional<QuizClass> classOpt = classRepository.findById(quizDto.getClassId());
        return Quiz.builder().id(quizDto.getId())
                .title(quizDto.getTitle())
                .exercises(exercises)
                .maxScore(quizDto.getMaxScore())
                .numberInClass(quizDto.getNumberInClass())
                .quizClass(classOpt.orElse(null))
                .active(quizDto.getActive())
                .repeatable(quizDto.getRepeatable())
                .build();
    }

    public QuizDto map(Quiz quiz) {
        Set<ExerciseDto> exercises;

        if (quiz.getExercises() == null || quiz.getExercises().isEmpty())
            exercises = new HashSet<>();

        else
            exercises = quiz.getExercises().stream()
                    .map(this::map)
                    .collect(Collectors.toSet());

        return QuizDto.builder().id(quiz.getId())
                .title(quiz.getTitle())
                .exercises(exercises)
                .maxScore(quiz.getMaxScore())
                .numberInClass(quiz.getNumberInClass())
                .repeatable(quiz.getRepeatable())
                .classId(quiz.getQuizClass() == null ? null : quiz.getQuizClass().getId())
                .active(quiz.getActive())
                .build();
    }

    public QuizWithScoreDto map(Quiz quiz, User user) {
        List<Score> scores = scoreService.getScores(user, quiz);
        boolean repeatable = quiz.getRepeatable() != null && quiz.getRepeatable();
        if (scores != null && !scores.isEmpty()) {
            Optional<Score> scoreOpt = scores.stream().max(Comparator.comparing(Score::getScore));
            return scoreOpt.map(s -> new QuizWithScoreDto(quiz.getId(), quiz.getTitle(), quiz.getMaxScore(), s.getScore(), user.getUsername(), repeatable, true))
                    .orElse(new QuizWithScoreDto(quiz.getId(), quiz.getTitle(), quiz.getMaxScore(), 0, user.getUsername(), repeatable, true));
        }
        return new QuizWithScoreDto(quiz.getId(), quiz.getTitle(), quiz.getMaxScore(), 0, user.getUsername(), true, false);
    }

    public Exercise map(ExerciseDto exerciseDto) {
        Optional<ExerciseType> typeOpt;
        if (exerciseDto.getType() != null)
            typeOpt = ExerciseType.findByName(exerciseDto.getType());
        else
            typeOpt = Optional.empty();
        Optional<Quiz> quizOpt;
        if (exerciseDto.getQuizId() != null)
            quizOpt = quizService.findById(exerciseDto.getQuizId());
        else
            quizOpt = Optional.empty();
        return new Exercise(exerciseDto.getId(), exerciseDto.getMaxScore(), exerciseDto.getQuestion(), exerciseDto.getAnswer(), exerciseDto.getWrongAnswers(), quizOpt.orElse(null), typeOpt.orElse(null), exerciseDto.getNumber());
    }

    public ExerciseDto map(Exercise exercise) {
        return new ExerciseDto(exercise.getId(), exercise.getMaxScore(), exercise.getQuestion(), exercise.getAnswer(), exercise.getWrongAnswers(), exercise.getQuiz().getId(), exercise.getType().getLabel(), exercise.getNumber());
    }

    public Score map(ScoreDto scoreDto) {
        Optional<User> user = userRepository.findById(scoreDto.getUserId());
        if (user.isPresent()) {
            Optional<Quiz> quiz = quizService.findById(scoreDto.getQuizId());
            if (quiz.isPresent()) {
                return new Score(scoreDto.getId(), user.get(), quiz.get(), scoreDto.getScore(), scoreDto.isQuizFinished());
            } else {
                log.error("Quiz with id: " + scoreDto.getQuizId() + " doesn't exist!");
            }
        } else {
            log.error("User with id: " + scoreDto.getUserId() + " doesn't exist!");
        }
        return null;
    }

    public ScoreDto map(Score score) {
        boolean quizFinished = score.getQuizFinished() != null && score.getQuizFinished();
        return new ScoreDto(score.getId(), score.getUser().getId(), score.getQuiz().getId(), score.getScore(), quizFinished);
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

        return new QuizClassDto(quizClass.getId(), quizClass.getTeacherId(), students, quizzes, quizClass.getTitle(), quizClass.getDescription());
    }

    public QuizClass map(QuizClassDto quizClassDto) {

        if (quizClassDto == null)
            return null;

        Optional<User> teacherOpt = userRepository.findById(quizClassDto.getTeacherId());
        Set<User> students = userRepository.findAllByIdIn(quizClassDto.getStudents());
        Set<Quiz> quizzes = quizService.findAllByIds(quizClassDto.getQuizList());

        if (teacherOpt.isEmpty()) {
            log.error("User with id: " + quizClassDto.getTeacherId() + " doesn't exist!");
            return null;
        }

        return new QuizClass(quizClassDto.getId(), teacherOpt.get().getId(), students, quizzes, quizClassDto.getTitle(), quizClassDto.getDescription());

    }

}
