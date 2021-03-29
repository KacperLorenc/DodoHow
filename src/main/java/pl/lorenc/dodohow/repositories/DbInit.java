package pl.lorenc.dodohow.repositories;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lorenc.dodohow.entities.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DbInit implements CommandLineRunner {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private QuizRepository quizRepository;
    private ExerciseRepository exerciseRepository;
    private ClassRepository classRepository;

    @Autowired
    public DbInit(UserRepository userRepository, PasswordEncoder passwordEncoder, QuizRepository quizRepository, ExerciseRepository exerciseRepository, ClassRepository classRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.quizRepository = quizRepository;
        this.exerciseRepository = exerciseRepository;
        this.classRepository = classRepository;
    }

    @Override
    public void run(String... args) {


        User admin = new User(null, "admin123", passwordEncoder.encode("admin123"), "admin@wp.pl", true, "ROLE_ADMIN");
        admin.setClasses(new HashSet<>());
        userRepository.save(admin);

        User t1 = new User(null, "kasia123", passwordEncoder.encode("kasia123"), "kasia@wp.pl", false, "ROLE_TEACHER");
        t1.setClasses(new HashSet<>());
        userRepository.save(t1);

        User user = new User(null, "kapa123", passwordEncoder.encode("kapa123"), "kacperlorenc1@wp.pl", true, "ROLE_USER");

        QuizClass dodohow = new QuizClass(null, admin.getId(), Sets.newHashSet(user), new HashSet<>(), "DodoHow", "Podstawy proponowowane przed dodohow");


        for (int i = 1; i < 30; i++) {
            User t2 = new User(null, "nauczyciel" + i, passwordEncoder.encode("nauczyciel" + i), "nauczyciel" + i + "@wp.pl", false, "ROLE_TEACHER");
            t2.setClasses(new HashSet<>());
            userRepository.save(t2);
        }


        //Sekcja nr0 - blueprint --------------------------------------------------------------------------------------------------
//        Exercise Ex1 = new Exercise(null, 1, "", "", "", null, ExerciseType.CHOOSE_ANSWER);
//        Exercise Ex1 = new Exercise(null, 1, "", "", "", null, ExerciseType.CHOOSE_ANSWER);
//        Exercise Ex3 = new Exercise(null, 1, "", "", "", null, ExerciseType.CHOOSE_ANSWER);
//        Exercise Ex4 = new Exercise(null, 1, "", "", "", null, ExerciseType.SELECT_WORDS);
//        Exercise Ex5 = new Exercise(null, 1, "", "", "", null, ExerciseType.SELECT_WORDS);
//        Exercise Ex6 = new Exercise(null, 1, "", "", "", null, ExerciseType.SELECT_WORDS);
//        Exercise Ex7 = new Exercise(null, 1, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex8 = new Exercise(null, 1, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex9 = new Exercise(null, 1, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex10 = new Exercise(null, 1, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex11 = new Exercise(null, 1, "", "", "", null, ExerciseType.TYPE_SENTENCE);
//        Exercise Ex11 = new Exercise(null, 1, "", "", "", null, ExerciseType.TRANSLATE_WORD);
//        Exercise Ex13 = new Exercise(null, 1, "", "", "", null, ExerciseType.TRANSLATE_WORD);
//        Exercise Ex14 = new Exercise(null, 1, "", "", "", null, ExerciseType.FILL_THE_BLANK);
//        Exercise Ex15 = new Exercise(null, 1, "", "", "", null, ExerciseType.FILL_THE_BLANK);
//
//        Set<Exercise> exercises = Sets.newHashSet(Ex1, Ex1, Ex3, Ex4, Ex5, Ex6, Ex7, Ex8, Ex9, Ex10, Ex11, Ex11, Ex13, Ex14, Ex15);
//        Section Sec = new Section(null, "", exercises,30,0);
//        exercisesBasic1.forEach(e -> e.setSection(Sec));
//        sectionRepository.save(Sec);

        //Sekcja nr1 - podstawy --------------------------------------------------------------------------------------------------
        Exercise basic1Ex1 = new Exercise(null, 1, "Mam na imię Jan", "My name is Jan", "My orange is Juan;My name is Jan;You eat Jan;She be Jan", null, ExerciseType.CHOOSE_ANSWER, 1);
        Exercise basic1Ex2 = new Exercise(null, 1, "Jestem mężczyzną", "I am a man", "You a man;I am a woman;He is an ant;I am a man", null, ExerciseType.CHOOSE_ANSWER, 15);
        Exercise basic1Ex3 = new Exercise(null, 1, "Ty jesteś kobietą", "You are a woman", "He cooks;You are a woman;You are an apple;She is a woman", null, ExerciseType.CHOOSE_ANSWER, 13);
        Exercise basic1Ex4 = new Exercise(null, 1, "I is a man", "false", "true", null, ExerciseType.TRUTH_FALSE, 11);
        Exercise basic1Ex5 = new Exercise(null, 1, "She is a girl", "true", "false", null, ExerciseType.TRUTH_FALSE, 5);
        Exercise basic1Ex6 = new Exercise(null, 1, "They are men", "true", "false", null, ExerciseType.TRUTH_FALSE, 4);
        Exercise basic1Ex7 = new Exercise(null, 1, "Jestem chłopcem", "I am a boy", "", null, ExerciseType.TYPE_SENTENCE, 8);
        Exercise basic1Ex8 = new Exercise(null, 1, "Ona jest dziewczyną", "She is a girl", "", null, ExerciseType.TYPE_SENTENCE, 2);
        Exercise basic1Ex9 = new Exercise(null, 1, "Jestem kobietą", "I am a woman", "", null, ExerciseType.TYPE_SENTENCE, 6);
        Exercise basic1Ex10 = new Exercise(null, 1, "Ty jesz", "You eat", "", null, ExerciseType.TYPE_SENTENCE, 12);
        Exercise basic1Ex11 = new Exercise(null, 1, "My jesteśmy kobietami", "We are women", "", null, ExerciseType.TYPE_SENTENCE, 3);
        Exercise basic1Ex12 = new Exercise(null, 1, "Jabłko", "An apple", "", null, ExerciseType.TRANSLATE_WORD, 9);
        Exercise basic1Ex13 = new Exercise(null, 1, "Dziewczyna", "A girl", "", null, ExerciseType.TRANSLATE_WORD, 14);
        Exercise basic1Ex14 = new Exercise(null, 1, "He ... a man", "is", "are;am;is;be", null, ExerciseType.FILL_THE_BLANK, 7);
        Exercise basic1Ex15 = new Exercise(null, 1, "We ... women", "are", "are;is;am;be", null, ExerciseType.FILL_THE_BLANK, 10);

        Set<Exercise> exercisesBasic1 = Sets.newHashSet(basic1Ex1, basic1Ex2, basic1Ex3, basic1Ex4, basic1Ex5, basic1Ex6, basic1Ex7, basic1Ex8, basic1Ex9, basic1Ex10, basic1Ex11, basic1Ex12, basic1Ex13, basic1Ex14, basic1Ex15);
        Quiz basicsSec = new Quiz(null, "Podstawy", exercisesBasic1, 15, 1, dodohow);
        basicsSec.setQuizClass(dodohow);
        exercisesBasic1.forEach(e -> e.setQuiz(basicsSec));

        //Sekcja nr1 - Zwierzęta --------------------------------------------------------------------------------------------------
        Exercise animalsEx1 = new Exercise(null, 1, "Kot i pies", "A cat and a dog", "A cat and a dog;An elephant and an ant;A dog and a duck", null, ExerciseType.CHOOSE_ANSWER, 1);
        Exercise animalsEx2 = new Exercise(null, 1, "Koty jedzą ryby", "Cats eat fish", "Fish eat cats;Ants eat elephants;Cats eat fish", null, ExerciseType.CHOOSE_ANSWER, 5);
        Exercise animalsEx3 = new Exercise(null, 1, "Jestem człowiekiem", "I am a human", "I are an apple;I am a human;She a duck", null, ExerciseType.CHOOSE_ANSWER, 10);
        Exercise animalsEx4 = new Exercise(null, 1, "Cats are animals", "true", "", null, ExerciseType.TRUTH_FALSE, 2);
        Exercise animalsEx5 = new Exercise(null, 1, "Dogs is animals", "false", "", null, ExerciseType.TRUTH_FALSE, 6);
        Exercise animalsEx6 = new Exercise(null, 1, "The elephant eat a apple", "false", "", null, ExerciseType.TRUTH_FALSE, 11);
        Exercise animalsEx7 = new Exercise(null, 1, "Ptaki jedzą chleb", "Birds eat bread", "", null, ExerciseType.TYPE_SENTENCE, 3);
        Exercise animalsEx8 = new Exercise(null, 1, "Ona ma kota", "She has a cat", "", null, ExerciseType.TYPE_SENTENCE, 7);
        Exercise animalsEx9 = new Exercise(null, 1, "To jest świnia", "It is a pig", "", null, ExerciseType.TYPE_SENTENCE, 12);
        Exercise animalsEx10 = new Exercise(null, 1, "Konie biegają", "Horses run", "", null, ExerciseType.TYPE_SENTENCE, 4);
        Exercise animalsEx11 = new Exercise(null, 1, "Kaczka jest ptakiem", "A duck is a bird", "", null, ExerciseType.TYPE_SENTENCE, 8);
        Exercise animalsEx12 = new Exercise(null, 1, "Psy", "Dogs", "", null, ExerciseType.TRANSLATE_WORD, 9);
        Exercise animalsEx13 = new Exercise(null, 1, "Świnia", "Pig", "", null, ExerciseType.TRANSLATE_WORD, 14);
        Exercise animalsEx14 = new Exercise(null, 1, "... bark", "Dogs", "Ants;Dogs;Cats", null, ExerciseType.FILL_THE_BLANK, 13);
        Exercise animalsEx15 = new Exercise(null, 1, "... purr", "Cats", "Elephants;Dogs;Cats", null, ExerciseType.FILL_THE_BLANK, 15);

        Set<Exercise> exercisesAnimals = Sets.newHashSet(animalsEx1, animalsEx2, animalsEx3, animalsEx4, animalsEx5, animalsEx6, animalsEx7, animalsEx8, animalsEx9, animalsEx10, animalsEx11, animalsEx12, animalsEx13, animalsEx14, animalsEx15);
        Quiz animalsSec = new Quiz(null, "Zwierzęta", exercisesAnimals, 15, 2, dodohow);
        animalsSec.setQuizClass(dodohow);
        exercisesAnimals.forEach(e -> e.setQuiz(animalsSec));

        //Sekcja nr3 - Jedzenie --------------------------------------------------------------------------------------------------
        Exercise foodEx1 = new Exercise(null, 1, "Oni mają pomarańcze", "They have oranges", "They are oranges;They have oranges;We have bread;You eat an orange", null, ExerciseType.CHOOSE_ANSWER, 15);
        Exercise foodEx2 = new Exercise(null, 1, "Jem kurczaka", "I eat chicken", "I have chicken;You have an apple;I eat chicken;She has a fork", null, ExerciseType.CHOOSE_ANSWER, 10);
        Exercise foodEx3 = new Exercise(null, 1, "Owoce i warzywa", "Fruits and vegetables", "Apple and pie;Fruits and vegetables;Cats and dogs;Fruits and fish", null, ExerciseType.CHOOSE_ANSWER, 5);
        Exercise foodEx4 = new Exercise(null, 1, "He has an orange", "true", "", null, ExerciseType.TRUTH_FALSE, 14);
        Exercise foodEx5 = new Exercise(null, 1, "I has an apple", "false", "", null, ExerciseType.TRUTH_FALSE, 9);
        Exercise foodEx6 = new Exercise(null, 1, "I eat dinner", "true", "", null, ExerciseType.TRUTH_FALSE, 4);
        Exercise foodEx7 = new Exercise(null, 1, "On gotuje", "He cooks", "", null, ExerciseType.TYPE_SENTENCE, 13);
        Exercise foodEx8 = new Exercise(null, 1, "Ser i chleb", "The cheese and the bread", "", null, ExerciseType.TYPE_SENTENCE, 8);
        Exercise foodEx9 = new Exercise(null, 1, "Ona pije wode", "She drinks water", "", null, ExerciseType.TYPE_SENTENCE, 3);
        Exercise foodEx10 = new Exercise(null, 1, "Wy pijecie sok", "You drink juice", "", null, ExerciseType.TYPE_SENTENCE, 11);
        Exercise foodEx11 = new Exercise(null, 1, "On je kanapkę", "He eats a sandwich", "", null, ExerciseType.TYPE_SENTENCE, 7);
        Exercise foodEx12 = new Exercise(null, 1, "Widelec", "A fork", "", null, ExerciseType.TRANSLATE_WORD, 1);
        Exercise foodEx13 = new Exercise(null, 1, "Talerz", "A plate", "", null, ExerciseType.TRANSLATE_WORD, 12);
        Exercise foodEx14 = new Exercise(null, 1, "She ... eggs", "has", "have;hat;has;is", null, ExerciseType.FILL_THE_BLANK, 6);
        Exercise foodEx15 = new Exercise(null, 1, "They ... beer", "drink", "eat;drink;are;is", null, ExerciseType.FILL_THE_BLANK, 2);

        Set<Exercise> exercises = Sets.newHashSet(foodEx1, foodEx2, foodEx3, foodEx4, foodEx5, foodEx6, foodEx7, foodEx8, foodEx9, foodEx10, foodEx11, foodEx12, foodEx13, foodEx14, foodEx15);
        Quiz foodSec = new Quiz(null, "Jedzenie", exercises, 15, 3, dodohow);
        foodSec.setQuizClass(dodohow);
        exercises.forEach(e -> e.setQuiz(foodSec));

        dodohow.setQuizzes(Sets.newHashSet(basicsSec, animalsSec, foodSec));
        userRepository.save(user);
        classRepository.save(dodohow);
        user.setClasses(new HashSet<>());
        user.getClasses().add(dodohow);
        userRepository.save(user);

        for (int i = 1; i < 30; i++) {
            User t2 = new User(null, "user" + i, passwordEncoder.encode("user" + i), "user" + i + "@wp.pl", true, "ROLE_USER");
            dodohow.getStudents().add(t2);
            t2.setClasses(new HashSet<>());
            t2.getClasses().add(dodohow);
            userRepository.save(t2);
        }
        userRepository.save(user);

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Użytkownicy:");
        userRepository.findAll().forEach(u -> {
            System.out.println("id:" + u.getId() + " " + "login:" + u.getUsername() + " " + "password:" + passwordEncoder.encode(u.getPassword()));
        });

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Klasy:");
        classRepository.findAll().forEach(c -> {
            System.out.println(c.getTitle());
            System.out.println("Nauczyciel: " + c.getTeacherId());

            System.out.println(quizRepository.findAllByIdIn(c.getQuizzes().stream().map(Quiz::getId).collect(Collectors.toList())));
            System.out.println(userRepository.findAllByIdIn(c.getStudents().stream().map(User::getId).collect(Collectors.toList())));
        });

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Ćwiczenia:");
        exerciseRepository.findAll().forEach(e -> {
            System.out.println(e.toString());
        });


    }
}
