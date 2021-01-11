package pl.olafszewczak.dodohow.services;

import com.google.common.collect.Sets;
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
        if (sections == null || sections.isEmpty()) {
            Optional<Section> sectionOpt = getFirstSection();
            return sectionOpt.<Set<Section>>map(Sets::newHashSet).orElseGet(HashSet::new);
        }
        Set<Long> ids = sections.stream()
                .map(Section::getId)
                .collect(Collectors.toSet());
        return sectionRepository.findAllByIdIn(ids);
    }

    public Optional<Section> findNextSection(User user) {
        Set<Section> sections = findUsersSections(user);

        return sections.stream()
                .map(s -> s.getSectionType().getNumber())
                .max(Integer::compareTo)
                .flatMap(number -> SectionType.findByNumber(number + 1).flatMap(type -> sectionRepository.findBySectionType(type)));
    }

    public Optional<Section> getFirstSection() {
        Optional<SectionType> sectionType = SectionType.findByNumber(1);
        if (sectionType.isPresent()) {
            return sectionRepository.findBySectionType(sectionType.get());
        }
        return Optional.empty();
    }

    public Set<Section> findAllByIds(Collection<Long> ids){
        return sectionRepository.findAllByIdIn(ids);
    }

    public Optional<Section> findById(Long id){
        return sectionRepository.findById(id);
    }

}
