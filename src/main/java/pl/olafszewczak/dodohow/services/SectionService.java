package pl.olafszewczak.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.olafszewczak.dodohow.entities.Score;
import pl.olafszewczak.dodohow.entities.Section;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.repositories.ScoreRepository;
import pl.olafszewczak.dodohow.repositories.SectionRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private SectionRepository sectionRepository;
    private ScoreRepository scoreRepository;

    @Autowired
    public SectionService(SectionRepository sectionRepository, ScoreRepository scoreRepository) {
        this.sectionRepository = sectionRepository;
        this.scoreRepository = scoreRepository;
    }

    public Set<Section> findUsersSections(User user) {
        Set<Section> sections = user.getSections();
        if (sections == null) {
            return new HashSet<>();
        }
        Set<Long> ids = sections.stream()
                .map(Section::getId)
                .collect(Collectors.toSet());
        return sectionRepository.findAllByIdIn(ids);
    }

    public Optional<Section> findNextSection(User user) {
        Set<Section> sections = findUsersSections(user);
        return sections.stream()
                .map(Section::getNumberInClass)
                .max(Integer::compareTo)
                .flatMap(number -> sectionRepository.findByNumberInClass(number + 1));
    }

    public Optional<Section> getFirstSection() {
        return sectionRepository.findByNumberInClass(1);
    }

    public Set<Section> findAllByIds(Collection<Long> ids) {
        return sectionRepository.findAllByIdIn(ids);
    }

    public Optional<Section> findById(Long id) {
        return sectionRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return sectionRepository.existsById(id);
    }

    public List<Score> getScores(User user) {
        return scoreRepository.findAllByUser(user);
    }

    public List<Score> getScores(User user, Section section) {
        return scoreRepository.findAllBySectionAndUser(section, user);
    }

    public Score saveScore(Score score){
        return scoreRepository.save(score);
    }

    public void deleteScore(User user, Section section){
        scoreRepository.deleteByUserAndSection(user, section);
    }

    public Optional<Score> findScore (User user, Section section){
        return scoreRepository.findByUserAndSection(user, section);
    }

}
