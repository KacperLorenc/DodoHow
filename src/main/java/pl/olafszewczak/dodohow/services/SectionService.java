package pl.olafszewczak.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.olafszewczak.dodohow.entities.Section;
import pl.olafszewczak.dodohow.entities.SectionType;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.repositories.SectionRepository;
import pl.olafszewczak.dodohow.repositories.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private SectionRepository sectionRepository;
    private UserRepository userRepository;

    @Autowired
    public SectionService(SectionRepository sectionRepository, UserRepository userRepository) {

        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Set<Section> getUsersSections(User user) {
        Set<Section> sections = user.getSections();
        if (sections == null || sections.isEmpty()) {
            Set<Section> newSections = new HashSet<>();
            getFirstSection().ifPresent(newSections::add);
            user.setSections(newSections);
            userRepository.save(user);
            return newSections;
        }
        Set<Long> ids = sections.stream()
                .map(Section::getId)
                .collect(Collectors.toSet());
        return sectionRepository.findAllByIdIn(ids);
    }

    public Optional<Section> getFirstSection() {
        return sectionRepository.findBySectionType(SectionType.FIRST_SECTION);
    }

}
