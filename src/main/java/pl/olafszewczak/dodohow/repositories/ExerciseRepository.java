package pl.olafszewczak.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.olafszewczak.dodohow.entities.Exercise;

import java.util.Set;

public interface ExerciseRepository extends CrudRepository<Exercise, Long> {

    public Set<Exercise> findAllById(Set<Long> id);
}
