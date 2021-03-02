package pl.lorenc.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lorenc.dodohow.entities.Score;
import pl.lorenc.dodohow.entities.Section;
import pl.lorenc.dodohow.entities.User;

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
