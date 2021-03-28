package pl.lorenc.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lorenc.dodohow.entities.Exercise;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
    Set<Exercise> findAllByIdIn(Collection<Long> id);
    List<Exercise> findAllByQuizId(Long id);
    Optional<Exercise> findByQuiz_IdAndNumber(Long id, Integer number);
    boolean existsByQuizIdAndNumber(Long quizId, Integer number);
}
