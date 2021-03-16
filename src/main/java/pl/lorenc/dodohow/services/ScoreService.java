package pl.lorenc.dodohow.services;

import org.springframework.stereotype.Service;
import pl.lorenc.dodohow.entities.Score;
import pl.lorenc.dodohow.entities.Section;
import pl.lorenc.dodohow.entities.User;
import pl.lorenc.dodohow.repositories.ScoreRepository;
import pl.lorenc.dodohow.repositories.SectionRepository;
import pl.lorenc.dodohow.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ScoreService {
    private SectionRepository sectionRepository;
    private UserRepository userRepository;
    private ScoreRepository scoreRepository;

    public ScoreService(SectionRepository sectionRepository, UserRepository userRepository, ScoreRepository scoreRepository) {
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
        this.scoreRepository = scoreRepository;
    }

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

    public List<Score> getScores(User user, Section section) {
        return scoreRepository.findAllBySectionAndUser(section, user);
    }

    public void deleteScore(User user, Section section) {
        scoreRepository.deleteByUserAndSection(user, section);
    }

    public Optional<Score> findScore(User user, Section section) {
        return scoreRepository.findByUserAndSection(user, section);
    }

    public boolean checkIfScoreExists(Section section, User user) {
        return scoreRepository.findByUserAndSection(user, section)
                .isPresent();
    }
}
