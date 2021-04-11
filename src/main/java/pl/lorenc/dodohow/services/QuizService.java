package pl.lorenc.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lorenc.dodohow.entities.Quiz;
import pl.lorenc.dodohow.entities.QuizClass;
import pl.lorenc.dodohow.entities.User;
import pl.lorenc.dodohow.repositories.QuizRepository;
import pl.lorenc.dodohow.repositories.ScoreRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
public class QuizService {

    private QuizRepository quizRepository;
    private ScoreRepository scoreRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository, ScoreRepository scoreRepository) {
        this.quizRepository = quizRepository;
        this.scoreRepository = scoreRepository;
    }

    public Optional<Quiz> findNextQuiz(User user, Long quizClassId) {
        Set<Quiz> quizzes = findAllByClassId(quizClassId, true);
        Optional<Quiz> currentLastQuiz = quizzes.stream()
                .map(Quiz::getNumberInClass)
                .max(Integer::compareTo)
                .flatMap(number -> quizRepository.findByNumberInClassAndQuizClassIdAndActive(number, quizClassId, true));
        if (currentLastQuiz.isPresent()) {
            if (existsByQuizAndUser(currentLastQuiz.get(), user)) {
                return quizRepository.findByNumberInClassAndQuizClassIdAndActive(currentLastQuiz.get().getNumberInClass() + 1, quizClassId, true);
            }
        }

        return Optional.empty();
    }

    public Optional<Quiz> getFirstQuiz(Long classId) {
        return quizRepository.findByNumberInClassAndQuizClassIdAndActive(1, classId, true);
    }

    public Set<Quiz> findAllByIds(Collection<Long> ids) {
        return quizRepository.findAllByIdIn(ids);
    }

    public Set<Quiz> findAll(Collection<Long> ids, QuizClass quizClass, boolean active) {
        return quizRepository.findAllByIdInAndQuizClassAndActive(ids, quizClass, active);
    }

    public Optional<Quiz> findById(Long id) {
        return quizRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return quizRepository.existsById(id);
    }

    public boolean existsByQuizAndUser(Quiz quiz, User user) {
        return scoreRepository.existsByUserAndQuiz(user, quiz);
    }

    public Set<Quiz> findAllByClassId(Long classId) {
        return quizRepository.findAllByQuizClassId(classId);
    }

    public Set<Quiz> findAllByClassId(Long classId, boolean active) {
        return quizRepository.findAllByQuizClassIdAndActive(classId, active);
    }

    @Transactional
    public void save(Quiz quiz) {
        quizRepository.save(quiz);
    }
}
