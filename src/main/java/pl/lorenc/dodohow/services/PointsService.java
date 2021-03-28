package pl.lorenc.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lorenc.dodohow.entities.Exercise;
import pl.lorenc.dodohow.entities.Points;
import pl.lorenc.dodohow.entities.User;
import pl.lorenc.dodohow.repositories.PointsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PointsService {

    private PointsRepository pointsRepository;

    @Autowired
    public PointsService(PointsRepository pointsRepository) {
        this.pointsRepository = pointsRepository;
    }

    @Transactional
    public Points save(Points point) {
        return pointsRepository.save(point);
    }

    public Optional<Points> findById(Long id) {
        return pointsRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return pointsRepository.existsById(id);
    }

    public Iterable<Points> findAll() {
        return pointsRepository.findAll();
    }

    public long count() {
        return pointsRepository.count();
    }
    @Transactional
    public void deleteById(Long id) {
        pointsRepository.deleteById(id);
    }
    @Transactional
    public void delete(Points points) {
        pointsRepository.delete(points);
    }
    @Transactional
    public void delete(User user, Exercise exercise) {
        pointsRepository.deleteByUserAndExercise(user, exercise);
    }

    public List<Points> findAllByExercisesAndUser(List<Exercise> exercises, User user) {
        return pointsRepository.findAllByExerciseInAndUser(exercises, user);
    }

    @Transactional
    public void deleteAllByUserAndExercises(User user, List<Exercise> exercises) {
        pointsRepository.deleteAllByUserAndExerciseIn(user, exercises);
    }

    public Optional<Points> findByUserAndExercise(User user, Exercise exercise) {
        return pointsRepository.findByUserAndExercise(user, exercise);
    }
}
