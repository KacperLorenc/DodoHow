package pl.lorenc.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lorenc.dodohow.dtos.QuizDto;
import pl.lorenc.dodohow.services.QuizFacade;

import javax.validation.Valid;

@Controller
public class QuizController {

    private final QuizFacade quizFacade;

    @Autowired
    public QuizController(QuizFacade quizFacade) {
        this.quizFacade = quizFacade;
    }

    @GetMapping("/classes/scores")
    public String getScores(Model model, @RequestParam("quiz") Long quizId) {
        try {
            return quizFacade.getScores(quizId, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    @GetMapping("/classes/activate")
    public String activateQuiz(@RequestParam("quiz") Long quizId) {
        try {
            return quizFacade.activateQuiz(quizId);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    @GetMapping("/classes/new-quiz")
    public String newQuiz(@RequestParam("class") Long id, Model model) {
        try {
            return quizFacade.newQuiz(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    @PostMapping("/classes/new-quiz")
    public String addQuiz(@ModelAttribute("quiz") @Valid QuizDto quizDto, Errors errors, @RequestParam("class") Long id, Model model) {
        try {
            if (errors.hasErrors()) {
                model.addAttribute("classId", id);
                return "teacher/newQuiz";
            }

            return quizFacade.addQuiz(quizDto, id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }
}
