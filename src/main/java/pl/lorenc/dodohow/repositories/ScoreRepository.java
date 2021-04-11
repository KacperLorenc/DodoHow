package pl.lorenc.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lorenc.dodohow.entities.Quiz;
import pl.lorenc.dodohow.entities.Score;
import pl.lorenc.dodohow.entities.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Long> {

    void deleteByDateTimeBeforeAndUser(LocalDateTime localDateTime, User user);
    List<Score> findAllByUser(User user);
    List<Score> findAllByQuizAndUser(Quiz quiz, User user);
    void deleteByUserAndQuiz(User user, Quiz quiz);
    Optional<Score> findByUserAndQuiz(User user, Quiz quiz);
    boolean existsByUserAndQuiz(User user, Quiz quiz);
    Set<Score> findAllByQuizInAndUser(Collection<Quiz> quizzes, User user);
    Set<Score> findAllByQuiz(Quiz quiz);
}
