package pl.olafszewczak.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.olafszewczak.dodohow.entities.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

}
