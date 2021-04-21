package pl.lorenc.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lorenc.dodohow.entities.Quiz;
import pl.lorenc.dodohow.entities.QuizClass;
import pl.lorenc.dodohow.entities.User;
import pl.lorenc.dodohow.repositories.QuizRepository;
import pl.lorenc.dodohow.repositories.ScoreRepository;

import java.util.*;

@Service
public class QuizService {

    private QuizRepository quizRepository;
    private ScoreRepository scoreRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository, ScoreRepository scoreRepository) {
        this.quizRepository = quizRepository;
        this.scoreRepository = scoreRepository;
    }

    public Optional<Quiz> findNextQuiz(Collection<Quiz> quizzes, Long quizClassId) {
        Set<Quiz> quizzesInClass = findAllByClassId(quizClassId, true);
        int highestNumber = quizzes.stream()
                .map(Quiz::getNumberInClass)
                .max(Integer::compareTo)
                .orElse(-1);
        if (highestNumber > 0) {
            return quizzesInClass.stream()
                    .filter(q -> q.getNumberInClass() > highestNumber)
                    .min(Comparator.comparing(Quiz::getNumberInClass));
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
