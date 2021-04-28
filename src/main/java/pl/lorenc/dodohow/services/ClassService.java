package pl.lorenc.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lorenc.dodohow.entities.QuizClass;
import pl.lorenc.dodohow.entities.User;
import pl.lorenc.dodohow.repositories.ClassRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class ClassService {

    private ClassRepository repository;

    @Autowired
    public ClassService(ClassRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public <S extends QuizClass> S save(S s) {
        return repository.save(s);
    }

    public Optional<QuizClass> findById(Long aLong) {
        return repository.findById(aLong);
    }

    public boolean existsById(Long aLong) {
        return repository.existsById(aLong);
    }

    public Iterable<QuizClass> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void deleteById(Long aLong) {
        repository.deleteById(aLong);
    }

    @Transactional
    public void delete(QuizClass quizClass) {
        repository.delete(quizClass);
    }

    public Set<QuizClass> findAllByUser(User user) {
        if (user.getRoleList().contains("ROLE_TEACHER") || user.getRoleList().contains("ROLE_ADMIN"))
            return repository.findAllByTeacherId(user.getId());

        return repository.findAllByStudentsContaining(user);
    }

    public boolean existsByTitle(String title) {
        return repository.existsByTitle(title);
    }

}
