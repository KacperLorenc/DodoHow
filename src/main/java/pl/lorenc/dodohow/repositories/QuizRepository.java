package pl.lorenc.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lorenc.dodohow.entities.Quiz;
import pl.lorenc.dodohow.entities.QuizClass;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface QuizRepository extends CrudRepository<Quiz, Long> {
    Set<Quiz> findAllByIdIn(Collection<Long> id);
    Optional<Quiz> findByNumberInClassAndQuizClassIdAndActive(Integer numberInClass, Long classId, Boolean active);
    Set<Quiz> findAllByQuizClassId(Long id);
    Set<Quiz> findAllByQuizClassIdAndActive(Long id, Boolean active);
    Set<Quiz> findAllByIdInAndQuizClassAndActive(Collection<Long> id, QuizClass quizClass, Boolean active);
}
