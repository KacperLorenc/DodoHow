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

        // Przykład --------------------------------------------------------------------------------------------
//        Exercise exercise1 = new Exercise(null, 10, "Powiedz tak po angielsku", "Yes", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise exercise2 = new Exercise(null, 10, "Powiedz nie po angielsku", "No", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise exercise3 = new Exercise(null, 10, "Jak powiesz\"Moje imię to Jan\" ?", "My name is Jan", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise exercise4 = new Exercise(null, 10, "Testowe zadanie 4", "test", "", null, ExerciseType.CHOOSE_ANSWER);
//        Exercise exercise5 = new Exercise(null, 10, "Testowe zadanie 5", "test", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise exercise6 = new Exercise(null, 10, "Testowe zadanie 6", "test", "", null, ExerciseType.SELECT_WORDS);
//        Exercise exercise7 = new Exercise(null, 10, "Testowe zadanie 7", "test", "", null, ExerciseType.CHOOSE_ANSWER);
//        Exercise exercise8 = new Exercise(null, 10, "Testowe zadanie 8", "test", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise exercise9 = new Exercise(null, 10, "Testowe zadanie 9", "test", "", null, ExerciseType.SELECT_WORDS);
//
//        Set<Exercise> exercises1 = Sets.newHashSet(exercise1, exercise2, exercise3);
//        Set<Exercise> exercises2 = Sets.newHashSet(exercise4, exercise5, exercise6);
//        Set<Exercise> exercises3 = Sets.newHashSet(exercise7, exercise8, exercise9);
//        Section section = new Section(null, "Podstawy", exercises1,50,1);
//        Section section2 = new Section(null,"Jedzenie", exercises2, 45, 2);
//        Section section3 = new Section(null,"Kuchnia", exercises3, 45, 3);
//        exercises1.forEach(e -> e.setSection(section));
//        exercises2.forEach(e -> e.setSection(section2));
//        exercises3.forEach(e -> e.setSection(section3));
//        sectionRepository.save(section);
//        sectionRepository.save(section2);
//        sectionRepository.save(section3);
//        userRepository.findById(user.getId()).ifPresent(u -> {
//            u.setSections(Sets.newHashSet(section, section2));
//            userRepository.save(u);
//        });

        //Sekcja nr0 - blueprint --------------------------------------------------------------------------------------------------
//        Exercise Ex1 = new Exercise(null, 2, "", "", "", null, ExerciseType.CHOOSE_ANSWER);
//        Exercise Ex2 = new Exercise(null, 2, "", "", "", null, ExerciseType.CHOOSE_ANSWER);
//        Exercise Ex3 = new Exercise(null, 2, "", "", "", null, ExerciseType.CHOOSE_ANSWER);
//        Exercise Ex4 = new Exercise(null, 2, "", "", "", null, ExerciseType.SELECT_WORDS);
//        Exercise Ex5 = new Exercise(null, 2, "", "", "", null, ExerciseType.SELECT_WORDS);
//        Exercise Ex6 = new Exercise(null, 2, "", "", "", null, ExerciseType.SELECT_WORDS);
//        Exercise Ex7 = new Exercise(null, 2, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex8 = new Exercise(null, 2, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex9 = new Exercise(null, 2, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex10 = new Exercise(null, 2, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex11 = new Exercise(null, 2, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex12 = new Exercise(null, 2, "", "", "", null, ExerciseType.TRANSLATE_WORD);
//        Exercise Ex13 = new Exercise(null, 2, "", "", "", null, ExerciseType.TRANSLATE_WORD);
//        Exercise Ex14 = new Exercise(null, 2, "", "", "", null, ExerciseType.FILL_THE_BLANK);
//        Exercise Ex15 = new Exercise(null, 2, "", "", "", null, ExerciseType.FILL_THE_BLANK);
//
//        Set<Exercise> exercises = Sets.newHashSet(Ex1, Ex2, Ex3, Ex4, Ex5, Ex6, Ex7, Ex8, Ex9, Ex10, Ex11, Ex12, Ex13, Ex14, Ex15);
//        Section Sec = new Section(null, "", exercises,30,0);
//        exercisesBasic1.forEach(e -> e.setSection(Sec));
//        sectionRepository.save(Sec);

        //Sekcja nr1 - podstawy --------------------------------------------------------------------------------------------------
        Exercise basic1Ex1 = new Exercise(null, 2, "Mam na imię Jan", "My name is Jan", "My orange is Juan;My name is Jan;You eat Jan", null, ExerciseType.CHOOSE_ANSWER);
        Exercise basic1Ex2 = new Exercise(null, 2, "Jestem mężczyzną", "I am a man", "I am a woman;He is an ant;I am a man", null, ExerciseType.CHOOSE_ANSWER);
        Exercise basic1Ex3 = new Exercise(null, 2, "Ty jesteś kobietą", "You are w woman", "You are a woman;You are an apple;She is a woman", null, ExerciseType.CHOOSE_ANSWER);
        Exercise basic1Ex4 = new Exercise(null, 2, "I is a man", "False", "", null, ExerciseType.TRUTH_FALSE);
        Exercise basic1Ex5 = new Exercise(null, 2, "She is a girl", "True", "", null, ExerciseType.TRUTH_FALSE);
        Exercise basic1Ex6 = new Exercise(null, 2, "They are men", "True", "True", null, ExerciseType.TRUTH_FALSE);
        Exercise basic1Ex7 = new Exercise(null, 2, "Jestem chłopcem", "I am a boy", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise basic1Ex8 = new Exercise(null, 2, "On jest dziewczyną", "She is a girl", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise basic1Ex9 = new Exercise(null, 2, "Jestem kobietą", "I am a woman", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise basic1Ex10 = new Exercise(null, 2, "Ty jesz", "You eat", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise basic1Ex11 = new Exercise(null, 2, "My jesteśmy kobietami", "We are women", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise basic1Ex12 = new Exercise(null, 2, "Jabłko", "An apple", "", null, ExerciseType.TRANSLATE_WORD);
        Exercise basic1Ex13 = new Exercise(null, 2, "Dziewczyna", "A Girl", "", null, ExerciseType.TRANSLATE_WORD);
        Exercise basic1Ex14 = new Exercise(null, 2, "He ... a man", "is", "are;am;is", null, ExerciseType.FILL_THE_BLANK);
        Exercise basic1Ex15 = new Exercise(null, 2, "We ... women", "are", "are;is;am", null, ExerciseType.FILL_THE_BLANK);

        Set<Exercise> exercisesBasic1 = Sets.newHashSet(basic1Ex1, basic1Ex2, basic1Ex3, basic1Ex4, basic1Ex5, basic1Ex6, basic1Ex7, basic1Ex8, basic1Ex9, basic1Ex10, basic1Ex11, basic1Ex12, basic1Ex13, basic1Ex14, basic1Ex15);
        Section basicsSec = new Section(null, "Podstawy", exercisesBasic1,30,1);
        exercisesBasic1.forEach(e -> e.setSection(basicsSec));
        sectionRepository.save(basicsSec);

        //Sekcja nr1 - Zwierzęta --------------------------------------------------------------------------------------------------
        Exercise animalsEx1 = new Exercise(null, 2, "Kot i pies", "A cat and a dog", "A cat and a dog;An elephant and an ant;A dog and a duck", null, ExerciseType.CHOOSE_ANSWER);
        Exercise animalsEx2 = new Exercise(null, 2, "Koty jedzą ryby", "Cats eat fish", "Fish eat cats;Ants eat elephants;Cats eat fish", null, ExerciseType.CHOOSE_ANSWER);
        Exercise animalsEx3 = new Exercise(null, 2, "Jestem człowiekiem", "I am a human", "I are an apple;I am a human;She a duck", null, ExerciseType.CHOOSE_ANSWER);
        Exercise animalsEx4 = new Exercise(null, 2, "Cats are animals", "True", "", null, ExerciseType.TRUTH_FALSE);
        Exercise animalsEx5 = new Exercise(null, 2, "Dogs is animals", "False", "", null, ExerciseType.TRUTH_FALSE);
        Exercise animalsEx6 = new Exercise(null, 2, "The elephant eat a apple", "False", "", null, ExerciseType.TRUTH_FALSE);
        Exercise animalsEx7 = new Exercise(null, 2, "Ptaki jedzą chleb", "Birds eat bread", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise animalsEx8 = new Exercise(null, 2, "Ona ma kota", "She has a cat", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise animalsEx9 = new Exercise(null, 2, "To jest świnia", "It is a pig", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise animalsEx10 = new Exercise(null, 2, "Słoń je jabłko", "The elephant eats an apple", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise animalsEx11 = new Exercise(null, 2, "Kaczka jest ptakiem", "A duck is a bird", "", null, ExerciseType.TYPE_SENTENCE);
        Exercise animalsEx12 = new Exercise(null, 2, "Psy", "Dogs", "", null, ExerciseType.TRANSLATE_WORD);
        Exercise animalsEx13 = new Exercise(null, 2, "Świnia", "Pig", "", null, ExerciseType.TRANSLATE_WORD);
        Exercise animalsEx14 = new Exercise(null, 2, "... bark", "Dogs", "Ants;Dogs;Cats", null, ExerciseType.FILL_THE_BLANK);
        Exercise animalsEx15 = new Exercise(null, 2, "... purr", "Cats", "Elephants;Dogs;Cats", null, ExerciseType.FILL_THE_BLANK);

        Set<Exercise> exercisesAnimals = Sets.newHashSet(animalsEx1, animalsEx2, animalsEx3, animalsEx4, animalsEx5, animalsEx6, animalsEx7, animalsEx8, animalsEx9, animalsEx10, animalsEx11, animalsEx12, animalsEx13, animalsEx14, animalsEx15);
        Section animalsSec = new Section(null, "Zwierzęta", exercisesAnimals,30,1);
        exercisesAnimals.forEach(e -> e.setSection(animalsSec));
        sectionRepository.save(animalsSec);

        userRepository.findById(user.getId()).ifPresent(u -> {
            u.setSections(Sets.newHashSet(basicsSec, animalsSec));
            userRepository.save(u);
        });

        //Sekcja nr3 - blueprint --------------------------------------------------------------------------------------------------
//        Exercise Ex1 = new Exercise(null, 2, "", "", "", null, ExerciseType.CHOOSE_ANSWER);
//        Exercise Ex2 = new Exercise(null, 2, "", "", "", null, ExerciseType.CHOOSE_ANSWER);
//        Exercise Ex3 = new Exercise(null, 2, "", "", "", null, ExerciseType.CHOOSE_ANSWER);
//        Exercise Ex4 = new Exercise(null, 2, "", "", "", null, ExerciseType.SELECT_WORDS);
//        Exercise Ex5 = new Exercise(null, 2, "", "", "", null, ExerciseType.SELECT_WORDS);
//        Exercise Ex6 = new Exercise(null, 2, "", "", "", null, ExerciseType.SELECT_WORDS);
//        Exercise Ex7 = new Exercise(null, 2, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex8 = new Exercise(null, 2, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex9 = new Exercise(null, 2, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex10 = new Exercise(null, 2, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex11 = new Exercise(null, 2, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex12 = new Exercise(null, 2, "", "", "", null, ExerciseType.TRANSLATE_WORD);
//        Exercise Ex13 = new Exercise(null, 2, "", "", "", null, ExerciseType.TRANSLATE_WORD);
//        Exercise Ex14 = new Exercise(null, 2, "", "", "", null, ExerciseType.FILL_THE_BLANK);
//        Exercise Ex15 = new Exercise(null, 2, "", "", "", null, ExerciseType.FILL_THE_BLANK);
//
//        Set<Exercise> exercises = Sets.newHashSet(Ex1, Ex2, Ex3, Ex4, Ex5, Ex6, Ex7, Ex8, Ex9, Ex10, Ex11, Ex12, Ex13, Ex14, Ex15);
//        Section Sec = new Section(null, "", exercises,30,0);
//        exercisesBasic1.forEach(e -> e.setSection(Sec));
//        sectionRepository.save(Sec);


//        sectionRepository.save(basicsSec);
//                userRepository.findById(user.getId()).ifPresent(u -> {
//            u.setSections(Sets.newHashSet(section, section2));
//            userRepository.save(u);
//        });



        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Użytkownicy:");
        userRepository.findAll().forEach(u -> {
            System.out.println("login:" + u.getUsername() + " " + "password:" + passwordEncoder.encode(u.getPassword()));
        });

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Sekcje:");
        sectionRepository.findAll().forEach(s -> {
            System.out.println(s.toString());
        });

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Ćwiczenia:");
        exerciseRepository.findAll().forEach(e -> {
            System.out.println(e.toString());
        });

    }
}
