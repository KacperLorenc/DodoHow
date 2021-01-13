package pl.olafszewczak.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.olafszewczak.dodohow.entities.Section;
import pl.olafszewczak.dodohow.entities.SectionType;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.repositories.SectionRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private SectionRepository sectionRepository;

    @Autowired
    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
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
                .map(Section::getId)
                .max(Long::compareTo)
                .flatMap(number -> sectionRepository.findById(number));
    }

    public Optional<Section> getFirstSection() {
        Optional<SectionType> sectionType = SectionType.findByNumber(1);
        if (sectionType.isPresent()) {
            return sectionRepository.findBySectionType(sectionType.get());
        }
        return Optional.empty();
    }

    public Set<Section> findAllByIds(Collection<Long> ids) {
        return sectionRepository.findAllByIdIn(ids);
    }

    public Optional<Section> findById(Long id) {
        return sectionRepository.findById(id);
    }

    public boolean existsById(Long id){
        return sectionRepository.existsById(id);
    }

}
