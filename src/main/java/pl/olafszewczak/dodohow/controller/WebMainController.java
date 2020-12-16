package pl.olafszewczak.dodohow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.olafszewczak.dodohow.util.Mappings;
import pl.olafszewczak.dodohow.util.ViewNames;

@Controller
public class WebMainController {




    // == request methods ==
    @GetMapping(Mappings.HOME)
    public String home() {
        return ViewNames.HOME;
    }



}
