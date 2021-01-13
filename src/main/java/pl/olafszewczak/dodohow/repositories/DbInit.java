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
        Exercise exercise4 = new Exercise(null, 10, "Testowe zadanie 4", "test", "", null, ExerciseType.CHOOSE_ANSWER);
        Exercise exercise5 = new Exercise(null, 10, "Testowe zadanie 5", "test", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise exercise6 = new Exercise(null, 10, "Testowe zadanie 6", "test", "", null, ExerciseType.SELECT_WORDS);
        Exercise exercise7 = new Exercise(null, 10, "Testowe zadanie 7", "test", "", null, ExerciseType.CHOOSE_ANSWER);
        Exercise exercise8 = new Exercise(null, 10, "Testowe zadanie 8", "test", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise exercise9 = new Exercise(null, 10, "Testowe zadanie 9", "test", "", null, ExerciseType.SELECT_WORDS);



        Set<Exercise> exercises1 = Sets.newHashSet(exercise1, exercise2, exercise3);
        Set<Exercise> exercises2 = Sets.newHashSet(exercise4, exercise5, exercise6);
        Set<Exercise> exercises3 = Sets.newHashSet(exercise7, exercise8, exercise9);
        Section section = new Section(null, "Podstawy", exercises1,50,1);
        Section section2 = new Section(null,"Jedzenie", exercises2, 45, 2);
        Section section3 = new Section(null,"Kuchnia", exercises3, 45, 3);
        exercises1.forEach(e -> e.setSection(section));
        exercises2.forEach(e -> e.setSection(section2));
        exercises3.forEach(e -> e.setSection(section3));
        sectionRepository.save(section);
        sectionRepository.save(section2);
        sectionRepository.save(section3);
        userRepository.findById(user.getId()).ifPresent(u -> {
            u.setSections(Sets.newHashSet(section, section2));
            userRepository.save(u);
        });


        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Użytkownicy:");
        userRepository.findAll().forEach(u -> {
            System.out.println("login:" + u.getUsername() + " " + "password:" + passwordEncoder.encode(u.getPassword()));
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
