package pl.olafszewczak.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.olafszewczak.dodohow.dtos.UserDto;
import pl.olafszewczak.dodohow.services.CredentialsService;

import javax.validation.Valid;

@Controller
public class CredentialsController {

    private CredentialsService credentialsService;

    @Autowired
    public CredentialsController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @GetMapping("/login")
    private String getLoginForm(Model model) {

        UserDto user = new UserDto();
        model.addAttribute("userDto", user);

        return "loginForm";
    }


    @PostMapping("/login")
    private String loginUser(@Valid UserDto userDto, Errors errors, Model model) {

        model.addAttribute("userDto", userDto);

        if(errors.hasErrors()){
            return "loginForm";
        }
        if (credentialsService.loginUser(userDto)) {
            return "redirect:/";
        }
        return "loginFormError";
    }

    @GetMapping("/register")
    private String getRegisterForm(Model model) {

        UserDto userDto = new UserDto();

        model.addAttribute("userDto", userDto);

        return "registerForm";
    }

    @PostMapping("/register")
    private String registerUser(@Valid UserDto userDto, Errors errors) {

        if(errors.hasErrors()){
            return "registerForm";
        }
        if (credentialsService.registerUser(userDto)) {
            return "loginFormSuccess";
        }
        return "registerFormError";
    }
}
