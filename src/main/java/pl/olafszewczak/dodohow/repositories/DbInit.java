package pl.olafszewczak.dodohow.repositories;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.olafszewczak.dodohow.entities.*;

import java.util.Set;

@Service
public class DbInit implements CommandLineRunner {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private SectionRepository sectionRepository;
    private ExerciseRepository exerciseRepository;

    @Autowired
    public DbInit(UserRepository userRepository, PasswordEncoder passwordEncoder, SectionRepository sectionRepository, ExerciseRepository exerciseRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sectionRepository = sectionRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(String... args) {

        User user = new User(null, "kapa123", passwordEncoder.encode("kapa123"), "kacperlorenc1@wp.pl", true, "ROLE_USER");
        userRepository.save(user);

        Exercise exercise1 = new Exercise(null, 10, "Powiedz tak po angielsku", "Yes", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise exercise2 = new Exercise(null, 10, "Powiedz nie po angielsku", "No", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise exercise3 = new Exercise(null, 10, "Jak powiesz\"Moje imię to Jan\" ?", "My name is Jan", "", null, ExerciseType.TYPE_SENTENCE);
        Set<Exercise> exercises = Sets.newHashSet(exercise1, exercise2, exercise3);
        Section section = new Section(null, "Podstawy", exercises, SectionType.FIRST_SECTION);
        exercises.forEach(e -> e.setSection(section));
        sectionRepository.save(section);

        userRepository.findById(user.getId()).ifPresent(u -> {
            u.setSections(Sets.newHashSet(section));
            userRepository.save(u);
        });


        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Użytkownicy:");
        userRepository.findAll().forEach(u -> {
            System.out.println("login:" + u.getUsername() + " " + "password:" + u.getPassword());
        });

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Sekcje:");
        sectionRepository.findAll().forEach(s -> {
            System.out.println(section.toString());
        });

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Ćwiczenia:");
        exerciseRepository.findAll().forEach(e -> {
            System.out.println(e.toString());
        });

    }
}
