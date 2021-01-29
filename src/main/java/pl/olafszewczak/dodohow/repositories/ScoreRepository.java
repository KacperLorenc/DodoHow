package pl.olafszewczak.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.olafszewczak.dodohow.entities.Score;
import pl.olafszewczak.dodohow.entities.Section;
import pl.olafszewczak.dodohow.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Long> {

    void deleteByDateTimeBeforeAndUser(LocalDateTime localDateTime, User user);
    List<Score> findAllByUser(User user);
    List<Score> findAllBySectionAndUser(Section section, User user);
    void deleteByUserAndSection(User user, Section section);
    Optional<Score> findByUserAndSection(User user, Section section);

}
