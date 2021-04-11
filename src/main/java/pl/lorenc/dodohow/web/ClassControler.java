package pl.lorenc.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import pl.lorenc.dodohow.dtos.QuizClassDto;
import pl.lorenc.dodohow.dtos.SearchDto;
import pl.lorenc.dodohow.services.ClassFacade;

import javax.validation.Valid;

@Controller
public class ClassControler {

    private ClassFacade classFacade;

    @Autowired
    public ClassControler(ClassFacade classFacade) {
        this.classFacade = classFacade;
    }

    @GetMapping("/classes")
    public String getClasses(Model model) {
        try {

            return classFacade.getClasses(model);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/classes/users/{id}")
    public String getUsers(@PathVariable Long id, Model model) {
        try {
            return classFacade.getUsers(id, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    @GetMapping("/new-class")
    public String newClass(Model model) {
        try {

            return classFacade.newClass(model);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    @PostMapping("/classes")
    public String addClass(@ModelAttribute("class") @Valid QuizClassDto quizClassDto, Errors errors) {
        try {
            if (errors.hasErrors()) {
                return "teacher/newClass";
            }

            return classFacade.addClass(quizClassDto);

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    @GetMapping("/classes/add-users/{classId}")
    public String getViewWithAllUsers(@PathVariable Long classId, Model model) {
        try {
            return classFacade.getViewWithAllUsers(classId, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    @GetMapping("/classes/add-user")
    public String addUserToClass(@RequestParam(value = "class") Long classId, @RequestParam(value = "user") Long userId) {
        try {
            return classFacade.addUserToClass(classId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    @PostMapping("/classes/add-users/{classId}")
    public String searchForUsers(@ModelAttribute("search") SearchDto searchDto, @PathVariable Long classId, Model model) {

        try {
            return classFacade.searchForUsers(searchDto, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    @GetMapping("/classes/add-users/{classId}/scores/{userId}")
    public String getScores(Model model, @PathVariable Long classId, @PathVariable Long userId) {
        try {
            return classFacade.getsScores(classId, userId, model);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    @GetMapping("/classes/delete-user")
    public String removeUserFromClass(@RequestParam(value = "class") Long classId, @RequestParam(value = "user") Long userId) {
        try {
            return classFacade.removeUserFromClass(userId, classId);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    @GetMapping("/classes/quizzes")
    public String getClassQuizzes(Model model, @RequestParam(value = "class") Long classId) {
        try {
                return classFacade.getClassQuizzes(model, classId);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }
}
