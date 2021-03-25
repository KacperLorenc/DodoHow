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
    private SectionService sectionService;
    private UserRepository userRepository;
    private ScoreService scoreService;


    @Autowired
    public DtoMapper(ExerciseRepository exerciseRepository, SectionService sectionService, UserRepository userRepository, ScoreService scoreService) {
        this.exerciseRepository = exerciseRepository;
        this.sectionService = sectionService;
        this.userRepository = userRepository;
        this.scoreService = scoreService;
    }

    public User map(UserDto userDto) {
        User user = new User(userDto.getId(), userDto.getLogin(), userDto.getPassword(), userDto.getEmail(), userDto.getActive(), userDto.getRoles());

        Set<SectionDto> sections = userDto.getSections();
        if (sections != null && !sections.isEmpty()) {
            Set<Long> ids = userDto.getSections().stream().map(SectionDto::getId).collect(Collectors.toSet());
            user.setSections(sectionService.findAllByIds(ids));
        }
        return user;
    }

    public UserDto map(User user) {

        boolean teacher = user.getRoleList()
                .contains("ROLE_TEACHER");

        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getActive(), user.getRoles(), teacher);
        Set<Section> sections = sectionService.findUsersSections(user);
        if (sections != null) {
            userDto.setSections(sections.stream()
                    .map(this::map)
                    .collect(Collectors.toSet()));
        }
        return userDto;
    }

    public Section map(SectionDto sectionDto) {
        if (sectionDto.getExercises() == null || sectionDto.getExercises().isEmpty()) {
            log.error("Section: " + sectionDto.getTitle() + " with id: " + sectionDto.getId() + " has no exercises");
        } else {
            Set<Exercise> exercises = exerciseRepository.findAllByIdIn(sectionDto.getExercises().stream().map(ExerciseDto::getId).collect(Collectors.toSet()));
            if (exercises == null || exercises.isEmpty()) {
                log.error("Section: " + sectionDto.getTitle() + " with id: " + sectionDto.getId() + " has wrong list of exercises");
            } else {
                return new Section(sectionDto.getId(), sectionDto.getTitle(), exercises, sectionDto.getMaxScore(), sectionDto.getNumberInClass());
            }
        }
        return null;
    }

    public SectionDto map(Section section) {
        Set<ExerciseDto> exercises = section.getExercises().stream()
                .map(this::map)
                .collect(Collectors.toSet());
        return new SectionDto(section.getId(), section.getTitle(), exercises, section.getMaxScore(), section.getNumberInClass());
    }

    public SectionWithScoreDto map(Section section, User user) {
        List<Score> scores = scoreService.getScores(user, section);
        if (scores != null && !scores.isEmpty()) {
            Optional<Score> scoreOpt = scores.stream().max(Comparator.comparing(Score::getScore));
            return scoreOpt.map(s -> new SectionWithScoreDto(section.getId(), section.getTitle(), section.getMaxScore(), s.getScore()))
                    .orElse(new SectionWithScoreDto(section.getId(), section.getTitle(), section.getMaxScore(), 0));
        }
        return new SectionWithScoreDto(section.getId(), section.getTitle(), section.getMaxScore(), 0);
    }

    public Exercise map(ExerciseDto exerciseDto) {
        Optional<ExerciseType> exerciseTypeOptional = ExerciseType.findByLabel(exerciseDto.getType());
        if (exerciseTypeOptional.isPresent()) {
            Optional<Section> sectionOptional = sectionService.findById(exerciseDto.getSectionId());
            if (sectionOptional.isPresent()) {
                return new Exercise(exerciseDto.getId(), exerciseDto.getMaxScore(), exerciseDto.getQuestion(), exerciseDto.getAnswer(), exerciseDto.getWrongAnswers(), sectionOptional.get(), exerciseTypeOptional.get(), exerciseDto.getNumber());
            } else {
                log.error("Section with id: " + exerciseDto.getSectionId() + " doesn't exist!");
            }
        } else {
            log.error("Exercise type: " + exerciseDto.getType() + " doesn't exist!");
        }
        return null;
    }

    public ExerciseDto map(Exercise exercise) {
        return new ExerciseDto(exercise.getId(), exercise.getMaxScore(), exercise.getQuestion(), exercise.getAnswer(), exercise.getWrongAnswers(), exercise.getSection().getId(), exercise.getType().getLabel(), exercise.getNumber());
    }

    public Score map(ScoreDto scoreDto) {
        Optional<User> user = userRepository.findById(scoreDto.getUserId());
        if (user.isPresent()) {
            Optional<Section> section = sectionService.findById(scoreDto.getSectionId());
            if (section.isPresent()) {
                return new Score(scoreDto.getId(), user.get(), section.get(), scoreDto.getScore());
            } else {
                log.error("Section with id: " + scoreDto.getSectionId() + " doesn't exist!");
            }
        } else {
            log.error("User with id: " + scoreDto.getUserId() + " doesn't exist!");
        }
        return null;
    }

    public ScoreDto map(Score score) {
        return new ScoreDto(score.getId(), score.getUser().getId(), score.getSection().getId(), score.getScore());
    }

    public QuizDto mapToQuiz(User user, Long sectionId) {
        return sectionService.findById(sectionId)
                .map(section -> new QuizDto(user.getId(), map(section)))
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

        Set<Long> sections = new HashSet<>();
        if (quizClass.getSections() != null) {
            sections = quizClass.getSections()
                    .stream()
                    .map(Section::getId)
                    .collect(Collectors.toSet());
        }

        return new QuizClassDto(quizClass.getId(), quizClass.getTeacher().getId(), students, sections);
    }

    public QuizClass map(QuizClassDto quizClassDto) {

        if (quizClassDto == null)
            return null;

        Optional<User> teacherOpt = userRepository.findById(quizClassDto.getTeacherId());
        Set<User> students = userRepository.findAllByIdIn(quizClassDto.getStudents());
        Set<Section> sections = sectionService.findAllByIds(quizClassDto.getSections());

        if (teacherOpt.isEmpty())
            log.error("User with id: " + quizClassDto.getTeacherId() + " doesn't exist!");

        return new QuizClass(quizClassDto.getId(), teacherOpt.orElse(null), students, sections);

    }

}
