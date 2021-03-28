package pl.lorenc.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lorenc.dodohow.entities.Quiz;
import pl.lorenc.dodohow.entities.User;
import pl.lorenc.dodohow.repositories.QuizRepository;
import pl.lorenc.dodohow.repositories.ScoreRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private QuizRepository quizRepository;
    private ScoreRepository scoreRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository, ScoreRepository scoreRepository) {
        this.quizRepository = quizRepository;
        this.scoreRepository = scoreRepository;
    }

    public Set<Quiz> findUsersQuizzes(User user) {
        Set<Quiz> quizzes = user.getQuizzes();
        if (quizzes == null) {
            return new HashSet<>();
        }
        Set<Long> ids = quizzes.stream()
                .map(Quiz::getId)
                .collect(Collectors.toSet());
        return quizRepository.findAllByIdIn(ids);
    }

    public Optional<Quiz> findNextQuiz(User user) {
        Set<Quiz> quizzes = findUsersQuizzes(user);
        Optional<Quiz> currentLastQuiz = quizzes.stream()
                .map(Quiz::getNumberInClass)
                .max(Integer::compareTo)
                .flatMap(number -> quizRepository.findByNumberInClass(number));
        if (currentLastQuiz.isPresent()) {
            if (existsByQuizAndUser(currentLastQuiz.get(), user)) {
                return quizRepository.findByNumberInClass(currentLastQuiz.get().getNumberInClass() + 1);
            }
        }

        return Optional.empty();
    }

    public Optional<Quiz> getFirstQuiz() {
        return quizRepository.findByNumberInClass(1);
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
