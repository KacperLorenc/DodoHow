package pl.olafszewczak.dodohow.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.olafszewczak.dodohow.dtos.*;
import pl.olafszewczak.dodohow.entities.*;
import pl.olafszewczak.dodohow.repositories.ExerciseRepository;
import pl.olafszewczak.dodohow.repositories.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DtoMapper {

    private ExerciseRepository exerciseRepository;
    private SectionService sectionService;
    private UserRepository userRepository;


    @Autowired
    public DtoMapper(ExerciseRepository exerciseRepository, SectionService sectionService, UserRepository userRepository) {
        this.exerciseRepository = exerciseRepository;
        this.sectionService = sectionService;
        this.userRepository = userRepository;
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
        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getActive(), user.getRoles());
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
        List<Score> scores = sectionService.getScores(user, section);
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


}
