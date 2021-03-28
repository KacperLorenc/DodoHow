package pl.lorenc.dodohow.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lorenc.dodohow.entities.Quiz;
import pl.lorenc.dodohow.entities.Score;
import pl.lorenc.dodohow.entities.User;
import pl.lorenc.dodohow.repositories.ScoreRepository;
import pl.lorenc.dodohow.repositories.QuizRepository;
import pl.lorenc.dodohow.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ScoreService {
    private QuizRepository quizRepository;
    private UserRepository userRepository;
    private ScoreRepository scoreRepository;

    public ScoreService(QuizRepository quizRepository, UserRepository userRepository, ScoreRepository scoreRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.scoreRepository = scoreRepository;
    }

    @Transactional
    public void saveScore(Score score) {
        userRepository.findById(score.getUser().getId()).ifPresent(user -> {
            Set<Score> scores = user.getScores();
            if (scores == null) {
                scores = new HashSet<>();
            }
            scores.add(score);
            score.setUser(user);
            userRepository.save(user);
        });
    }

    public List<Score> getScores(User user) {
        return scoreRepository.findAllByUser(user);
    }

    public List<Score> getScores(User user, Quiz quiz) {
        return scoreRepository.findAllByQuizAndUser(quiz, user);
    }

    @Transactional
    public void deleteScore(User user, Quiz quiz) {
        scoreRepository.deleteByUserAndQuiz(user, quiz);
    }

    public Optional<Score> findScore(User user, Quiz quiz) {
        return scoreRepository.findByUserAndQuiz(user, quiz);
    }

    public boolean checkIfScoreExists(Quiz quiz, User user) {
        return scoreRepository.findByUserAndQuiz(user, quiz)
                .isPresent();
    }
}
