package pl.lorenc.dodohow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lorenc.dodohow.entities.QuizClass;
import pl.lorenc.dodohow.repositories.ClassRepository;

import java.util.Optional;

@Service
public class ClassService {

    private ClassRepository repository;

    @Autowired
    public ClassService(ClassRepository repository) {
        this.repository = repository;
    }

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

    public void deleteById(Long aLong) {
        repository.deleteById(aLong);
    }

    public void delete(QuizClass quizClass) {
        repository.delete(quizClass);
    }

}
