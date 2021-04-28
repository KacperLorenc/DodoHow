package pl.lorenc.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lorenc.dodohow.entities.QuizClass;
import pl.lorenc.dodohow.entities.User;

import java.util.Collection;
import java.util.Set;

@Repository
public interface ClassRepository extends CrudRepository<QuizClass, Long> {
    Set<QuizClass> findAllByStudentsContaining(User user);

    Set<QuizClass> findAllByTeacherId(Long id);

    Set<QuizClass> findAllByIdIn(Collection<Long> ids);

    boolean existsByTitle(String title);

}
