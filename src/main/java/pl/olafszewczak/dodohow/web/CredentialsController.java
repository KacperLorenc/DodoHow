package pl.olafszewczak.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private String getLoginForm(Model model) {

        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);

        return "login-form";
    }


    @PostMapping("/login")
    private String loginUser(@ModelAttribute UserDto userDto) {
        if (credentialsService.loginUser(userDto)) {
            return "redirect:/";
        }
        return "login-form-error";
    }

    @GetMapping("/register")
    private String getRegisterForm(Model model) {

        model.addAttribute("user", new UserDto());

        return "register-form";
    }

    @PostMapping("/register")
    private String registerUser(@ModelAttribute UserDto userDto) {
        if (credentialsService.registerUser(userDto)) {
            return "login-form-success";
        }
        return "register-form-error";
    }
}
