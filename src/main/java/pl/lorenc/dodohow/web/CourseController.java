package pl.lorenc.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lorenc.dodohow.dtos.ExerciseDto;
import pl.lorenc.dodohow.services.CourseFacade;

@Controller
public class CourseController {

    private CourseFacade courseFacade;

    @Autowired
    public CourseController(CourseFacade courseFacade) {
        this.courseFacade = courseFacade;
    }

    @GetMapping("/course/{id}")
    public String newQuiz(@PathVariable Long id, Model model) {
        try {
            return courseFacade.newQuiz(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/quizzes";
        }
    }

    @PostMapping("/next-exercise")
    public String nextExercise(@ModelAttribute("exercise") ExerciseDto exercise) {
        try {
            return courseFacade.nextExercise(exercise);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/quizzes";
        }
    }

    @GetMapping("/exercise")
    public String getExercise(@RequestParam Long id, Model model) {
        try {
            return courseFacade.getExercise(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/quizzes";
        }
    }

    @GetMapping("/next-exercise")
    public String getNextExercise(@RequestParam Long id, Model model) {
        try {
            return courseFacade.getNextExercise(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/quizzes";
        }
    }

    @GetMapping("/previous-exercise")
    public String getPreviousExercise(@RequestParam Long id, Model model) {
        try {
            return courseFacade.getPreviousExercise(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/quizzes";
        }
    }
}
