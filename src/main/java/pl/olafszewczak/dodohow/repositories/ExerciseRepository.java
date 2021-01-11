package pl.olafszewczak.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.olafszewczak.dodohow.entities.Exercise;

import java.util.Collection;
import java.util.Set;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
    Set<Exercise> findAllByIdIn(Collection<Long> id);
}
