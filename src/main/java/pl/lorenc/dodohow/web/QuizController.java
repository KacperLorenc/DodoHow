package pl.lorenc.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.lorenc.dodohow.services.QuizFacade;

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
}
