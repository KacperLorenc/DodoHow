package pl.olafszewczak.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.olafszewczak.dodohow.dtos.ExerciseDto;
import pl.olafszewczak.dodohow.dtos.SectionDto;
import pl.olafszewczak.dodohow.dtos.UserDto;
import pl.olafszewczak.dodohow.entities.*;
import pl.olafszewczak.dodohow.repositories.ExerciseRepository;
import pl.olafszewczak.dodohow.repositories.SectionRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DtoMapper {
    private SectionRepository sectionRepository;
    private ExerciseRepository exerciseRepository;

    @Autowired
    public DtoMapper(SectionRepository sectionRepository, ExerciseRepository exerciseRepository) {
        this.sectionRepository = sectionRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public User map(UserDto userDto) {
        User user = new User(userDto.getId(), userDto.getLogin(), userDto.getPassword(), userDto.getEmail(), userDto.getActive(), userDto.getRoles());
        Set<Long> ids = userDto.getSections().stream().map(SectionDto::getId).collect(Collectors.toSet());
        user.setSections(sectionRepository.findAllByIdIn(ids));
        return user;
    }

    public UserDto map(User user) {
        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getActive(), user.getRoles());
        Set<Section> sections = user.getSections();
        if (sections != null) {
            userDto.setSections(sections.stream()
                    .map(this::map)
                    .collect(Collectors.toSet()));
        }
        return userDto;
    }

    public Section map(SectionDto sectionDto) {
        if (sectionDto.getExercises() == null || sectionDto.getExercises().isEmpty()) {
            throw new RuntimeException("Section: " + sectionDto.getTitle() + " with id: " + sectionDto.getId() + " has no exercises");
        } else {
            Set<Exercise> exercises = exerciseRepository.findAllByIdIn(sectionDto.getExercises().stream().map(ExerciseDto::getId).collect(Collectors.toSet()));
            if (exercises == null || exercises.isEmpty()) {
                throw new RuntimeException("Section: " + sectionDto.getTitle() + " with id: " + sectionDto.getId() + " has wrong list of exercises");
            } else {
                Optional<SectionType> sectionType = SectionType.findByLabel(sectionDto.getType());
                if (sectionType.isPresent()) {
                    return new Section(sectionDto.getId(), sectionDto.getTitle(), exercises, sectionType.get());
                } else {
                    throw new RuntimeException("Section type: " + sectionDto.getType() + " doesn't exist!");
                }
            }
        }
    }

    public SectionDto map(Section section) {
        Set<ExerciseDto> exercises = section.getExercises().stream()
                .map(this::map)
                .collect(Collectors.toSet());
        return new SectionDto(section.getId(), section.getTitle(), exercises, section.getSectionType().getLabel());
    }

    public Exercise map(ExerciseDto exerciseDto) {
        Optional<ExerciseType> exerciseTypeOptional = ExerciseType.findByLabel(exerciseDto.getType());
        if (exerciseTypeOptional.isPresent()) {
            Optional<Section> sectionOptional = sectionRepository.findById(exerciseDto.getSectionId());
            if (sectionOptional.isPresent()) {
                return new Exercise(exerciseDto.getId(), exerciseDto.getMaxScore(), exerciseDto.getQuestion(), exerciseDto.getAnswer(), exerciseDto.getWrongAnswers(), sectionOptional.get(), exerciseTypeOptional.get());
            } else {
                throw new RuntimeException("Section with id: " + exerciseDto.getSectionId() + " doesn't exist!");
            }
        } else {
            throw new RuntimeException("Exercise type: " + exerciseDto.getType() + " doesn't exist!");
        }
    }

    public ExerciseDto map(Exercise exercise) {
        return new ExerciseDto(exercise.getId(), exercise.getMaxScore(), exercise.getQuestion(), exercise.getAnswer(), exercise.getWrongAnswers(), exercise.getSection().getId(), exercise.getType().getLabel());
    }


}
