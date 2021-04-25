package pl.lorenc.dodohow.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lorenc.dodohow.entities.QuizClass;
import pl.lorenc.dodohow.entities.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Set<User> findAllByIdIn(Collection<Long> id);

    Set<User> findAllByActiveAndRolesContainsAndUsernameContaining(boolean active, String role, String username);

    Set<User> findAllByActiveAndRolesContainsAndUsernameContainingAndIdNotIn(boolean active, String role, String username, List<Long> ids);

    Set<User> findAllByActiveAndRolesContainsAndUsernameContainingAndIdIn(boolean active, String role, String username, List<Long> ids);

    Set<User> findAllByActiveAndRolesContainingAndIdIn(boolean active, String role, List<Long> ids);

    Set<User> findAllByActiveAndRolesContains(boolean active, String role);

    Set<User> findAllByActiveAndRolesContainsAndIdNotIn(boolean active, String role, List<Long> ids);

    Set<User> findAllByClassesContaining(QuizClass quizClass);

    @Modifying
    @Query("update User u set u.active = :active where u.id = :id")
    void updateUser(@Param(value = "id") Long id, @Param(value = "active") boolean active);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.classes WHERE u.id = :id")
    Optional<User> findByIdWithClasses(@Param(value = "id") Long id);



}
