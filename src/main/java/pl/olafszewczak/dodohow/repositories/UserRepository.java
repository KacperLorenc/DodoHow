package pl.olafszewczak.dodohow.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.olafszewczak.dodohow.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
