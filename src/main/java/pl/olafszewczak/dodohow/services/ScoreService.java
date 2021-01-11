package pl.olafszewczak.dodohow.services;

import org.springframework.stereotype.Service;
import pl.olafszewczak.dodohow.entities.Score;
import pl.olafszewczak.dodohow.repositories.SectionRepository;
import pl.olafszewczak.dodohow.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class ScoreService {
    private SectionRepository sectionRepository;
    private UserRepository userRepository;

    public ScoreService(SectionRepository sectionRepository, UserRepository userRepository) {
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
    }

    public void addScore(Long userId, Long sectionId, Integer score) {
        userRepository.findById(userId)
                .ifPresent(user -> sectionRepository.findById(sectionId).ifPresent(section -> {
                    Score newScore = new Score(null, user, section, score, LocalDateTime.now());
                    Set<Score> scores = user.getScores();
                    if (scores == null) {
                        scores = new HashSet<>();
                    }
                    scores.add(newScore);
                    newScore.setUser(user);
                    userRepository.save(user);
                }));
    }

    public void addScore(Score score) {
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
}
