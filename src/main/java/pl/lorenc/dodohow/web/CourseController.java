package pl.lorenc.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String newCourse(@PathVariable Long id, Model model) {
        try {
            return courseFacade.newCourse(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/sections";
        }
    }

    @PostMapping("/next-exercise")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String nextExercise(@ModelAttribute("exercise") ExerciseDto exercise) {
        try {
            return courseFacade.nextExercise(exercise);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/sections";
        }
    }

    @GetMapping("/exercise")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getExercise(@RequestParam Long id, Model model) {
        try {
            return courseFacade.getExercise(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/sections";
        }
    }

    @GetMapping("/next-exercise")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getNextExercise(@RequestParam Long id, Model model) {
        try {
            return courseFacade.getNextExercise(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/sections";
        }
    }

    @GetMapping("/previous-exercise")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getPreviousExercise(@RequestParam Long id, Model model) {
        try {
            return courseFacade.getPreviousExercise(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/sections";
        }
    }
}
