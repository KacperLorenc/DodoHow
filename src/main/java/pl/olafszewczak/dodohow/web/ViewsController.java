package pl.olafszewczak.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.olafszewczak.dodohow.entities.Section;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.services.DtoMapper;
import pl.olafszewczak.dodohow.services.SectionService;
import pl.olafszewczak.dodohow.services.UserService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ViewsController {

    private UserService userService;
    private SectionService sectionService;
    private DtoMapper mapper;

    @Autowired
    public ViewsController(UserService userService, SectionService sectionService, DtoMapper mapper) {
        this.userService = userService;
        this.sectionService = sectionService;
        this.mapper = mapper;
    }


    @GetMapping("/")
    public String getHome() {
        return "home/index";
    }

    @GetMapping("/sections")
    public String getSections(Model model) {
        try {
            Optional<User> userOptional = userService.getUserFromSession();
            return userOptional.map(user -> {
                Set<Section> userSections = sectionService.findUsersSections(user); //znajduje sekcje uzytkownika
                if (userSections.isEmpty()) {
                    sectionService.getFirstSection().ifPresent(userSections::add); //jesli nie ma zadnych przerobionych to przypisuje pierwsza sekcje
                } else {
                    sectionService.findNextSection(user).ifPresent(userSections::add); //jesli ma jakas przerobiona to dodaje kolejna
                }
                model.addAttribute("user", mapper.map(user));
                model.addAttribute("sections", userSections.stream().map(mapper::map).collect(Collectors.toList()));
                return "sections/sections";
            }).orElse("redirect:/login");
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/sections/section")
    public String getSection(Model model, Long sectionId) {
        try {
            Optional<User> userOptional = userService.getUserFromSession();
            return userOptional.map(user -> {
                if (sectionService.existsById(sectionId)) {
                    model.addAttribute("quiz", mapper.mapToQuiz(user, sectionId));
                    return "sections/quiz";         //jeśli znalazło sekcje i uzytkownika to zwraca dobry template
                } else {
                    return "redirect:/";
                }
            }).orElse("redirect:/login");

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
}
