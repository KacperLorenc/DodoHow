package pl.lorenc.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import pl.lorenc.dodohow.dtos.ExerciseDto;
import pl.lorenc.dodohow.dtos.ExerciseTypeDto;
import pl.lorenc.dodohow.services.CourseFacade;

import javax.validation.Valid;

@Controller
public class CourseController {

    private CourseFacade courseFacade;

    @Autowired
    public CourseController(CourseFacade courseFacade) {
        this.courseFacade = courseFacade;
    }

    @GetMapping("/quizzes/{id}")
    public String getQuizzes(@PathVariable Long id, Model model) {
        try {
            return courseFacade.getQuizzes(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/quizzes/quiz/{id}")
    public String getQuiz(@PathVariable Long id, Model model) {
        try {
            return courseFacade.getQuiz(model, id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/course/{id}")
    public String newQuiz(@PathVariable Long id, Model model) {
        try {
            return courseFacade.newQuiz(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/next-exercise")
    public String nextExercise(@ModelAttribute("exercise") ExerciseDto exercise) {
        try {
            return courseFacade.nextExercise(exercise);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/exercise")
    public String getExercise(@RequestParam(name = "number") Long id, Model model) {
        try {
            return courseFacade.getExercise(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/next-exercise")
    public String getNextExercise(@RequestParam Long id) {
        try {
            return courseFacade.getNextExercise(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/previous-exercise")
    public String getPreviousExercise(@RequestParam Long id) {
        try {
            return courseFacade.getPreviousExercise(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/redirect-summary")
    public String redirectToSummary(@RequestParam Long id) {
        try {
            return courseFacade.redirectSummary(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/summary")
    public String getSummary(@RequestParam(name = "quiz") Long id, Model model) {
        try {
            return courseFacade.summary(model, id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/exercises/new-exercise")
    public String newExercise(@RequestParam(name = "type") String type, @RequestParam(name = "quiz") Long id, Model model) {
        try {
            return courseFacade.newExercise(type, id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/exercises")
    public String chooseExerciseType(@ModelAttribute("typeobject") ExerciseTypeDto exerciseType) {
        try {
            return courseFacade.chooseExerciseType(exerciseType);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/exercises/choose-type")
    public String chooseType(@RequestParam(name = "quiz") Long id, Model model) {
        try {
            return courseFacade.chooseType(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/exercises/translate")
    public String addExerciseTranslateWord(@ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, Errors errors, @RequestParam(name = "quiz") Long id) {
        try {
            if (errors.hasErrors()) {
                return "exercises/translateWordform";
            }
            return courseFacade.addExerciseTranslateWord(exerciseDto, id);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/exercises/typein")
    public String addExerciseTypeIn(@ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, Errors errors, @RequestParam(name = "quiz") Long id) {
        try {
            if (errors.hasErrors()) {
                return "exercises/typeSentenceform";
            }
            return courseFacade.addExerciseTypeIn(exerciseDto, id);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/exercises/fillblank")
    public String addExerciseFillBlank(@ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, Errors errors, @RequestParam(name = "quiz") Long id) {
        try {
            if (errors.hasErrors()) {
                return "exercises/fillTheBlankform";
            }
            return courseFacade.addExerciseFillBlank(exerciseDto, id);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/exercises/choose")
    public String addExerciseChooseAnswer(@ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, Errors errors, @RequestParam(name = "quiz") Long id) {
        try {
            if (errors.hasErrors()) {
                return "exercises/chooseAnswerform";
            }
            return courseFacade.addExerciseChooseAnswer(exerciseDto, id);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/exercises/truefalse")
    public String addExerciseTrueFalse(@ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, Errors errors, @RequestParam(name = "quiz") Long id) {
        try {
            if (errors.hasErrors()) {
                return "exercises/truthFalseform";
            }
            return courseFacade.addExerciseTrueFalse(exerciseDto, id);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

}
