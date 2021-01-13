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
        Optional<User> user = userService.getUserFromSession();
        return user.map(u -> {
            Set<Section> userSections = sectionService.findUsersSections(u);
            if(userSections.isEmpty()){
                sectionService.getFirstSection().ifPresent(userSections::add);
            } else {
                sectionService.findNextSection(u).ifPresent(userSections::add);
            }
            model.addAttribute("user", mapper.map(u));
            return "sections/sections";
        }).orElse("redirect:/login");
    }

    @GetMapping("/chooseAnswer")
    public String getChooseAnswer() {
        return "exercises/choose-answer";
    }

    @GetMapping("/selectWords")
    public String getSelectWords() {
        return "exercises/select-words";
    }

    @GetMapping("/typeSentence")
    public String getTypeSentence() {
        return "exercises/type-sentence";
    }



}
