package pl.lorenc.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lorenc.dodohow.entities.Exercise;
import pl.lorenc.dodohow.repositories.ExerciseRepository;

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

    @Transactional
    public <S extends Exercise> S save(S exercise) {
        return exerciseRepository.save(exercise);
    }

    public Optional<Exercise> findById(Long id) {
        return exerciseRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        exerciseRepository.deleteById(id);
    }

    @Transactional
    public void delete(Exercise exercise) {
        exerciseRepository.delete(exercise);
    }

    public List<Exercise> findAllBy(Long quizId){
        return exerciseRepository.findAllByQuizId(quizId);
    }

    public Optional<Exercise> findByQuizIdAndNumber(Long quizId, Integer number){
        return exerciseRepository.findByQuiz_IdAndNumber(quizId, number);
    }

    public boolean existsByQuizIdAndNumber(Long quizId, Integer number) {
        return exerciseRepository.existsByQuizIdAndNumber(quizId, number);
    }
}
