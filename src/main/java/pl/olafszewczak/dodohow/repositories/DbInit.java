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
        Exercise basic1Ex1 = new Exercise(null, 2, "Mam na imię Jan", "My name is Jan", "My orange is Juan;My name is Jan;You eat Jan;She be Jan", null, ExerciseType.CHOOSE_ANSWER,1);
        Exercise basic1Ex2 = new Exercise(null, 2, "Jestem mężczyzną", "I am a man", "You a man;I am a woman;He is an ant;I am a man", null, ExerciseType.CHOOSE_ANSWER,2);
        Exercise basic1Ex3 = new Exercise(null, 2, "Ty jesteś kobietą", "You are a woman", "You are a woman;You are an apple;She is a woman", null, ExerciseType.CHOOSE_ANSWER,3);
        Exercise basic1Ex4 = new Exercise(null, 2, "I is a man", "false", "true", null, ExerciseType.TRUTH_FALSE,4);
        Exercise basic1Ex5 = new Exercise(null, 2, "She is a girl", "true", "false", null, ExerciseType.TRUTH_FALSE,5);
        Exercise basic1Ex6 = new Exercise(null, 2, "They are men", "true", "false", null, ExerciseType.TRUTH_FALSE,6);
        Exercise basic1Ex7 = new Exercise(null, 2, "Jestem chłopcem", "I am a boy", "", null, ExerciseType.TYPE_SENTENCE,7);
        Exercise basic1Ex8 = new Exercise(null, 2, "Ona jest dziewczyną", "She is a girl", "", null, ExerciseType.TYPE_SENTENCE,8);
        Exercise basic1Ex9 = new Exercise(null, 2, "Jestem kobietą", "I am a woman", "", null, ExerciseType.TYPE_SENTENCE,9);
        Exercise basic1Ex10 = new Exercise(null, 2, "Ty jesz", "You eat", "", null, ExerciseType.TYPE_SENTENCE,10);
        Exercise basic1Ex11 = new Exercise(null, 2, "My jesteśmy kobietami", "We are women", "", null, ExerciseType.TYPE_SENTENCE,11);
        Exercise basic1Ex12 = new Exercise(null, 2, "Jabłko", "An apple", "", null, ExerciseType.TRANSLATE_WORD,12);
        Exercise basic1Ex13 = new Exercise(null, 2, "Dziewczyna", "A girl", "", null, ExerciseType.TRANSLATE_WORD,13);
        Exercise basic1Ex14 = new Exercise(null, 2, "He ... a man", "is", "are;am;is;be", null, ExerciseType.FILL_THE_BLANK,14);
        Exercise basic1Ex15 = new Exercise(null, 2, "We ... women", "are", "are;is;am;be", null, ExerciseType.FILL_THE_BLANK,15);

        Set<Exercise> exercisesBasic1 = Sets.newHashSet(basic1Ex1, basic1Ex2, basic1Ex3, basic1Ex4, basic1Ex5, basic1Ex6, basic1Ex7, basic1Ex8, basic1Ex9, basic1Ex10, basic1Ex11, basic1Ex12, basic1Ex13, basic1Ex14, basic1Ex15);
        Section basicsSec = new Section(null, "Podstawy", exercisesBasic1,30,1);
        exercisesBasic1.forEach(e -> e.setSection(basicsSec));
        sectionRepository.save(basicsSec);

        //Sekcja nr2 - Zwierzęta --------------------------------------------------------------------------------------------------
        Exercise animalsEx1 = new Exercise(null, 2, "Kot i pies", "A cat and a dog", "A cat and a dog;An elephant and an ant;A dog and a duck", null, ExerciseType.CHOOSE_ANSWER,1);
        Exercise animalsEx2 = new Exercise(null, 2, "Koty jedzą ryby", "Cats eat fish", "Fish eat cats;Ants eat elephants;Cats eat fish", null, ExerciseType.CHOOSE_ANSWER,2);
        Exercise animalsEx3 = new Exercise(null, 2, "Jestem człowiekiem", "I am a human", "I are an apple;I am a human;She a duck", null, ExerciseType.CHOOSE_ANSWER,3);
        Exercise animalsEx4 = new Exercise(null, 2, "Cats are animals", "true", "", null, ExerciseType.TRUTH_FALSE,4);
        Exercise animalsEx5 = new Exercise(null, 2, "Dogs is animals", "false", "", null, ExerciseType.TRUTH_FALSE,5);
        Exercise animalsEx6 = new Exercise(null, 2, "The elephant eat a apple", "false", "", null, ExerciseType.TRUTH_FALSE,6);
        Exercise animalsEx7 = new Exercise(null, 2, "Ptaki jedzą chleb", "Birds eat bread", "", null, ExerciseType.TYPE_SENTENCE,7);
        Exercise animalsEx8 = new Exercise(null, 2, "Ona ma kota", "She has a cat", "", null, ExerciseType.TYPE_SENTENCE,8);
        Exercise animalsEx9 = new Exercise(null, 2, "To jest świnia", "It is a pig", "", null, ExerciseType.TYPE_SENTENCE,9);
        Exercise animalsEx10 = new Exercise(null, 2, "Konie biegają", "Horses run", "", null, ExerciseType.TYPE_SENTENCE,10);
        Exercise animalsEx11 = new Exercise(null, 2, "Kaczka jest ptakiem", "A duck is a bird", "", null, ExerciseType.TYPE_SENTENCE,11);
        Exercise animalsEx12 = new Exercise(null, 2, "Psy", "Dogs", "", null, ExerciseType.TRANSLATE_WORD,12);
        Exercise animalsEx13 = new Exercise(null, 2, "Świnia", "Pig", "", null, ExerciseType.TRANSLATE_WORD,13);
        Exercise animalsEx14 = new Exercise(null, 2, "... bark", "Dogs", "Ants;Dogs;Cats", null, ExerciseType.FILL_THE_BLANK,14);
        Exercise animalsEx15 = new Exercise(null, 2, "... purr", "Cats", "Elephants;Dogs;Cats", null, ExerciseType.FILL_THE_BLANK,15);

        Set<Exercise> exercisesAnimals = Sets.newHashSet(animalsEx1, animalsEx2, animalsEx3, animalsEx4, animalsEx5, animalsEx6, animalsEx7, animalsEx8, animalsEx9, animalsEx10, animalsEx11, animalsEx12, animalsEx13, animalsEx14, animalsEx15);
        Section animalsSec = new Section(null, "Zwierzęta", exercisesAnimals,30,2);
        exercisesAnimals.forEach(e -> e.setSection(animalsSec));
        sectionRepository.save(animalsSec);

        //Sekcja nr3 - Jedzenie --------------------------------------------------------------------------------------------------
        Exercise foodEx1 = new Exercise(null, 2, "Oni mają pomarańcze", "They have oranges", "They are oranges;They have oranges;We have bread", null, ExerciseType.CHOOSE_ANSWER,1);
        Exercise foodEx2 = new Exercise(null, 2, "Jem kurczaka", "I eat chicken", "", null, ExerciseType.CHOOSE_ANSWER,2);
        Exercise foodEx3 = new Exercise(null, 2, "Owoce i warzywa", "Fruits and vegetables", "Apple and pie;Fruits and vegetables;Cats and dogs;Fruits and fish", null, ExerciseType.CHOOSE_ANSWER,3);
        Exercise foodEx4 = new Exercise(null, 2, "He has an orange", "true", "", null, ExerciseType.TRUTH_FALSE,4);
        Exercise foodEx5 = new Exercise(null, 2, "I has an apple", "false", "", null, ExerciseType.TRUTH_FALSE,5);
        Exercise foodEx6 = new Exercise(null, 2, "I eat dinner", "true", "", null, ExerciseType.TRUTH_FALSE,6);
        Exercise foodEx7 = new Exercise(null, 2, "On gotuje", "He cooks", "", null, ExerciseType.TYPE_SENTENCE,7);
        Exercise foodEx8 = new Exercise(null, 2, "Ser i chleb", "The cheese and the bread", "", null, ExerciseType.TYPE_SENTENCE,8);
        Exercise foodEx9 = new Exercise(null, 2, "Ona pije wode", "She drinks water", "", null, ExerciseType.TYPE_SENTENCE,9);
        Exercise foodEx10 = new Exercise(null, 2, "Wy pijecie sok", "You drink juice", "", null, ExerciseType.TYPE_SENTENCE,10);
        Exercise foodEx11 = new Exercise(null, 2, "On je kanapkę", "He eats a sandwich", "", null, ExerciseType.TYPE_SENTENCE,11);
        Exercise foodEx12 = new Exercise(null, 2, "Widelec", "A fork", "", null, ExerciseType.TRANSLATE_WORD,12);
        Exercise foodEx13 = new Exercise(null, 2, "Talerz", "A plate", "", null, ExerciseType.TRANSLATE_WORD,13);
        Exercise foodEx14 = new Exercise(null, 2, "She ... eggs", "has", "have;hat;has;is", null, ExerciseType.FILL_THE_BLANK,14);
        Exercise foodEx15 = new Exercise(null, 2, "They ... beer", "drink", "eat;drink;are;is", null, ExerciseType.FILL_THE_BLANK,15);

        Set<Exercise> exercises = Sets.newHashSet(foodEx1, foodEx2, foodEx3, foodEx4, foodEx5, foodEx6, foodEx7, foodEx8, foodEx9, foodEx10, foodEx11, foodEx12, foodEx13, foodEx14, foodEx15);
        Section foodSec = new Section(null, "Jedzenie", exercises,30,3);
        exercisesBasic1.forEach(e -> e.setSection(foodSec));
        sectionRepository.save(foodSec);

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
