package pl.olafszewczak.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.olafszewczak.dodohow.entities.Section;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SectionRepository extends CrudRepository<Section, Long> {
    Set<Section> findAllByIdIn(Collection<Long> id);
    Optional<Section> findByNumberInClass(Integer numberInClass);
}
