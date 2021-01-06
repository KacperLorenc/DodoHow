package pl.olafszewczak.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.olafszewczak.dodohow.dtos.UserDto;
import pl.olafszewczak.dodohow.entities.User;
import pl.olafszewczak.dodohow.security.OnRegistrationCompleteEvent;
import pl.olafszewczak.dodohow.security.VerificationToken;
import pl.olafszewczak.dodohow.services.CredentialsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class CredentialsController {

    private CredentialsService credentialsService;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public CredentialsController(CredentialsService credentialsService, ApplicationEventPublisher eventPublisher) {
        this.credentialsService = credentialsService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/login")
    private String login() {
        return "login/login";
    }

    @GetMapping("/register")
    private String getRegisterForm(Model model) {

        UserDto userDto = new UserDto();

        model.addAttribute("user", userDto);

        return "login/register";
    }

    @PostMapping("/register")
    private String registerUser(@ModelAttribute("user") @Valid UserDto userDto, Errors errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return "login/register";
        }
        if (credentialsService.registerUser(userDto)) {
            String appUrl = request.getContextPath();
            Optional<User> userOpt = credentialsService.getByUsername(userDto.getLogin());
            userOpt.ifPresent(user -> eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl)));
            return "login/loginFormSuccess";
        }
        return "login/registerFormError";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(Model model, @RequestParam("token") String token) {

        Optional<VerificationToken> tokenOptional = credentialsService.getVerificationToken(token);
        if (tokenOptional.isEmpty()) {
            return "redirect:/wrong-token";
        }

        VerificationToken verificationToken = tokenOptional.get();

        User user = verificationToken.getUser();
        if (verificationToken.getExpiryDate().compareTo(LocalDate.now()) <= 0) {
            return "redirect:/wrong-token";
        }

        user.setActive(true);
        credentialsService.saveRegisteredUser(user);
        return "redirect:/login";
    }

    @GetMapping("wrong-token")
    public String wrongToken(Model model){
        model.addAttribute("message", "Twój link wygasł lub jest nieprawidłowy.");
        return "login/badUser";
    }

}
