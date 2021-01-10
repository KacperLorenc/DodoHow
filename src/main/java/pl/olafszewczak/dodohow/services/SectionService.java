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

    @Transactional
    public void assignNextSection(User user) {
        Set<Section> sections = getUsersSections(user);
        if (sections == null || sections.isEmpty()) {
            Optional<Section> sectionOptional = getFirstSection();
            sectionOptional.ifPresent(s -> {
                user.getSections().add(s);
                userRepository.save(user);
            });
        } else {
            sections.stream()
                    .map(s -> s.getSectionType().getNumber())
                    .max(Integer::compareTo)
                    .flatMap(number -> SectionType.findByNumber(number + 1).flatMap(type -> sectionRepository.findBySectionType(type)))
                    .ifPresent(section -> {
                        user.getSections().add(section);
                        userRepository.save(user);
                    });
        }
    }

    public Optional<Section> getFirstSection() {
        Optional<SectionType> sectionType = SectionType.findByNumber(1);
        if (sectionType.isPresent()) {
            return sectionRepository.findBySectionType(sectionType.get());
        }
        return Optional.empty();
    }
}
