package pl.olafszewczak.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.olafszewczak.dodohow.entities.Exercise;
import pl.olafszewczak.dodohow.repositories.ExerciseRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ExerciseService {

    private ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Set<Exercise> findAllByIdIn(List<Long> id) {
        return exerciseRepository.findAllByIdIn(id);
    }

    public <S extends Exercise> S save(S exercise) {
        return exerciseRepository.save(exercise);
    }

    public Optional<Exercise> findById(Long id) {
        return exerciseRepository.findById(id);
    }

    public void deleteById(Long id) {
        exerciseRepository.deleteById(id);
    }

    public void delete(Exercise exercise) {
        exerciseRepository.delete(exercise);
    }

    public List<Exercise> findAllBy(Long sectionId){
        return exerciseRepository.findAllBySectionId(sectionId);
    }

    public Optional<Exercise> findNextExercise(Long sectionId, Integer number){
        return exerciseRepository.findBySection_IdAndNumber(sectionId, number);
    }
}
