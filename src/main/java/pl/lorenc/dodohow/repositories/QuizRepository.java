package pl.lorenc.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lorenc.dodohow.entities.Quiz;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface QuizRepository extends CrudRepository<Quiz, Long> {
    Set<Quiz> findAllByIdIn(Collection<Long> id);
    Optional<Quiz> findByNumberInClassAndQuizClassId(Integer numberInClass, Long classId);
    Set<Quiz> findAllByQuizClassId(Long id);
}
