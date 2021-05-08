package pl.lorenc.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import pl.lorenc.dodohow.dtos.ExerciseDto;
import pl.lorenc.dodohow.dtos.ExerciseTypeDto;
import pl.lorenc.dodohow.services.ExerciseFacade;

import javax.validation.Valid;

@Controller
public class ExerciseController {

    private ExerciseFacade exerciseFacade;

    @Autowired
    public ExerciseController(ExerciseFacade exerciseFacade) {
        this.exerciseFacade = exerciseFacade;
    }

    @GetMapping("/quizzes/{id}")
    public String getQuizzes(@PathVariable Long id, Model model) {
        try {
            return exerciseFacade.getQuizzes(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/quizzes/quiz/{id}")
    public String getQuiz(@PathVariable Long id, Model model) {
        try {
            return exerciseFacade.getQuiz(model, id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/course/{id}")
    public String newQuiz(@PathVariable Long id, Model model) {
        try {
            return exerciseFacade.newQuiz(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/next-exercise")
    public String nextExercise(@ModelAttribute("exercise") ExerciseDto exercise) {
        try {
            return exerciseFacade.nextExercise(exercise);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/exercise")
    public String getExercise(@RequestParam(name = "number") Long id, Model model) {
        try {
            return exerciseFacade.getExercise(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/next-exercise")
    public String getNextExercise(@RequestParam Long id) {
        try {
            return exerciseFacade.getNextExercise(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/previous-exercise")
    public String getPreviousExercise(@RequestParam Long id) {
        try {
            return exerciseFacade.getPreviousExercise(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/redirect-summary")
    public String redirectToSummary(@RequestParam Long id) {
        try {
            return exerciseFacade.redirectSummary(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/summary")
    public String getSummary(@RequestParam(name = "quiz") Long id, Model model) {
        try {
            return exerciseFacade.summary(model, id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/exercises/new-exercise")
    public String newExercise(@RequestParam(name = "type") String type, @RequestParam(name = "quiz") Long id, Model model) {
        try {
            return exerciseFacade.newExercise(type, id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/exercises")
    public String chooseExerciseType(@ModelAttribute("typeobject") ExerciseTypeDto exerciseType) {
        try {
            return exerciseFacade.chooseExerciseType(exerciseType);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("exercises/update")
    public String updateExercise(@RequestParam(name = "exercise") Long id, Model model) {
        try {
            return exerciseFacade.updateExercise(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/exercises/choose-type")
    public String chooseType(@RequestParam(name = "quiz") Long id, Model model) {
        try {
            return exerciseFacade.chooseType(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("exercises/delete")
    public String deleteExercise(@RequestParam(name = "exercise") Long id) {
        try {
            return exerciseFacade.deleteExercise(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/exercises/translate")
    public String addExerciseTranslateWord(@ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, Errors errors, @RequestParam(name = "quiz") Long id, Model model) {
        try {
            if (errors.hasErrors()) {
                model.addAttribute("quizId", id);
                return "exercises/translateWordform";
            }
            return exerciseFacade.addExerciseTranslateWord(exerciseDto, id);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/exercises/typein")
    public String addExerciseTypeIn(@ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, Errors errors, @RequestParam(name = "quiz") Long id, Model model) {
        try {
            if (errors.hasErrors()) {
                model.addAttribute("quizId", id);
                return "exercises/typeSentenceform";
            }
            return exerciseFacade.addExerciseTypeIn(exerciseDto, id);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/exercises/fillblank")
    public String addExerciseFillBlank(@ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, Errors errors, @RequestParam(name = "quiz") Long id, Model model) {
        try {
            if (errors.hasErrors()) {
                model.addAttribute("quizId", id);
                return "exercises/fillTheBlankform";
            }
            return exerciseFacade.addExerciseFillBlank(exerciseDto, id);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/exercises/choose")
    public String addExerciseChooseAnswer(@ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, Errors errors, @RequestParam(name = "quiz") Long id, Model model) {
        try {
            if (errors.hasErrors()) {
                model.addAttribute("quizId", id);
                return "exercises/chooseAnswerform";
            }
            return exerciseFacade.addExerciseChooseAnswer(exerciseDto, id);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @PostMapping("/exercises/truefalse")
    public String addExerciseTrueFalse(@ModelAttribute("exercise") @Valid ExerciseDto exerciseDto, Errors errors, @RequestParam(name = "quiz") Long id, Model model) {
        try {
            if (errors.hasErrors()) {
                exerciseDto.setWrongAnswers("true;false");
                model.addAttribute("quizId", id);
                return "exercises/truthFalseform";
            }
            return exerciseFacade.addExerciseTrueFalse(exerciseDto, id);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

}
