package pl.lorenc.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lorenc.dodohow.entities.Section;
import pl.lorenc.dodohow.entities.User;
import pl.lorenc.dodohow.repositories.ScoreRepository;
import pl.lorenc.dodohow.repositories.SectionRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
        Optional<Section> currentLastSection = sections.stream()
                .map(Section::getNumberInClass)
                .max(Integer::compareTo)
                .flatMap(number -> sectionRepository.findByNumberInClass(number));
        if (currentLastSection.isPresent()) {
            if (checkIfScoreExists(currentLastSection.get(), user)) {
                return sectionRepository.findByNumberInClass(currentLastSection.get().getNumberInClass() + 1);
            }
        }

        return Optional.empty();
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

    public boolean checkIfScoreExists(Section section, User user) {
        return scoreRepository.findByUserAndSection(user, section)
                .isPresent();
    }
}
