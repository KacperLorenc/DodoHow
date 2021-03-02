package pl.lorenc.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lorenc.dodohow.entities.Exercise;
import pl.lorenc.dodohow.entities.Points;
import pl.lorenc.dodohow.entities.User;

import java.util.List;

@Repository
public interface PointsRepository extends CrudRepository<Points, Long> {

    void deleteByUserAndExercise(User user, Exercise exercise);
    void deleteAllByUserAndExerciseIn(User user, List<Exercise> exercises);
    List<Points> findAllByExerciseInAndUser(List<Exercise> exercises, User user);
}
