package pl.olafszewczak.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.olafszewczak.dodohow.dtos.UserDto;
import pl.olafszewczak.dodohow.services.CredentialsService;

@Controller
public class CredentialsController {

    private CredentialsService credentialsService;

    @Autowired
    public CredentialsController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @GetMapping("/login")
    private String getLoginForm(){

        return "login-form";
    }

    @GetMapping("/register")
    private String getRegisterForm(Model model){

        model.addAttribute("user", new UserDto());

        return "register-form";
    }

    @PostMapping("/register")
    private String registerUser(@ModelAttribute UserDto userDto){
        credentialsService.registerUser(userDto);

        return "login-form";
    }
}
