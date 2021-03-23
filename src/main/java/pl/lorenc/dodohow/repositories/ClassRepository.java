package pl.lorenc.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lorenc.dodohow.entities.QuizClass;

@Repository
public interface ClassRepository extends CrudRepository<QuizClass, Long> {
}
