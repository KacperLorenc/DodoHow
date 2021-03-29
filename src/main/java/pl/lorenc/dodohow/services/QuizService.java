package pl.lorenc.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lorenc.dodohow.entities.Quiz;
import pl.lorenc.dodohow.entities.User;
import pl.lorenc.dodohow.repositories.QuizRepository;
import pl.lorenc.dodohow.repositories.ScoreRepository;

import java.util.Collection;
import java.util.HashSet;
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

    public Set<Quiz> findUsersQuizzes(User user, Long quizClassId) {

        if (user.getClasses() == null || user.getClasses().isEmpty()) {
            return new HashSet<>();
        }
        if (user.getClasses().stream().anyMatch(c -> c.getId().equals(quizClassId))) {
            Set<Quiz> quizzes = quizRepository.findAllByQuizClassId(quizClassId);
            if (quizzes == null) {
                return new HashSet<>();
            }
            return quizzes;
        }
        return new HashSet<>();
    }

    public Optional<Quiz> findNextQuiz(User user, Long quizClassId) {
        Set<Quiz> quizzes = findUsersQuizzes(user, quizClassId);
        Optional<Quiz> currentLastQuiz = quizzes.stream()
                .map(Quiz::getNumberInClass)
                .max(Integer::compareTo)
                .flatMap(number -> quizRepository.findByNumberInClassAndQuizClassId(number, quizClassId));
        if (currentLastQuiz.isPresent()) {
            if (existsByQuizAndUser(currentLastQuiz.get(), user)) {
                return quizRepository.findByNumberInClassAndQuizClassId(currentLastQuiz.get().getNumberInClass() + 1, quizClassId);
            }
        }

        return Optional.empty();
    }

    public Optional<Quiz> getFirstQuiz(Long classId) {
        return quizRepository.findByNumberInClassAndQuizClassId(1, classId);
    }

    public Set<Quiz> findAllByIds(Collection<Long> ids) {
        return quizRepository.findAllByIdIn(ids);
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
}
