package pl.olafszewczak.dodohow.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewsController {

    @GetMapping("/")
    public String getHome(){
        return "home";
    }
}
