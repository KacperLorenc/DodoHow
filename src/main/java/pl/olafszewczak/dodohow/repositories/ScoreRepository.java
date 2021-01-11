package pl.olafszewczak.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.olafszewczak.dodohow.entities.Score;
import pl.olafszewczak.dodohow.entities.User;

import java.time.LocalDateTime;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Long> {

    void deleteByDateTimeBeforeAndUser(LocalDateTime localDateTime, User user);
    void findAllByUser(User user);

}
