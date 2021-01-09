package pl.olafszewczak.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.olafszewczak.dodohow.entities.Section;
import pl.olafszewczak.dodohow.entities.SectionType;

import java.util.Optional;
import java.util.Set;

public interface SectionRepository extends CrudRepository<Section, Long> {
    Set<Section> findAllById(Set<Long> id);
    Optional<Section> findBySectionType(SectionType sectionType);
}
