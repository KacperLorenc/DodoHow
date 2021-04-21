package pl.lorenc.dodohow.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import pl.lorenc.dodohow.dtos.UserDto;
import pl.lorenc.dodohow.dtos.UserListDto;
import pl.lorenc.dodohow.entities.User;
import pl.lorenc.dodohow.security.OnRegistrationCompleteEvent;
import pl.lorenc.dodohow.security.VerificationToken;
import pl.lorenc.dodohow.services.DtoMapper;
import pl.lorenc.dodohow.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class CredentialsController {

    private UserService userService;
    private ApplicationEventPublisher eventPublisher;
    private DtoMapper mapper;

    @Autowired
    public CredentialsController(UserService userService, ApplicationEventPublisher eventPublisher, DtoMapper mapper) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    @GetMapping("/login")
    private String login(Model model) {

        if (model.containsAttribute("user"))
            return "login/loginFormSuccess";

        return userService.getUserFromSession()
                .map(u -> "home/index")
                .orElse("login/login");
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
        if (userService.registerUser(userDto)) {
            if (userDto.getTeacher() == null || !userDto.getTeacher()) {
                String appUrl = request.getContextPath();
                Optional<User> userOpt = userService.findByUsername(userDto.getLogin());
                userOpt.ifPresent(user -> eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl)));
            }
            return "redirect:login";
        }
        return "login/registerFormError";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(Model model, @RequestParam("token") String token) {

        Optional<VerificationToken> tokenOptional = userService.getVerificationToken(token);
        if (tokenOptional.isEmpty()) {
            return "redirect:/wrong-token";
        }

        VerificationToken verificationToken = tokenOptional.get();

        User user = verificationToken.getUser();
        if (verificationToken.getExpiryDate().compareTo(LocalDate.now()) <= 0) {
            return "redirect:/wrong-token";
        }

        user.setActive(true);
        userService.saveRegisteredUser(user);
        return "redirect:/login";
    }

    @GetMapping("/wrong-token")
    public String wrongToken(Model model) {
        model.addAttribute("message", "Twój link wygasł lub jest nieprawidłowy.");
        return "login/badUser";
    }


    @GetMapping("/teachers/{id}")
    public String activateTeacher(@PathVariable Long id, Model model) {
        try {
            if (id != null)
                userService.activateUser(id);
            List<UserDto> t = userService.findUsersBy(false, "ROLE_TEACHER")
                    .stream()
                    .map(mapper::map)
                    .sorted(Comparator.comparing(UserDto::getLogin))
                    .collect(Collectors.toList());
            model.addAttribute("teacherSet", new UserListDto(t));
            return "home/teachers";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/teachers";
        }
    }

    @GetMapping("/delete-teacher/{id}")
    public String deleteTeacher(@PathVariable Long id, Model model) {
        try {
            if (id != null)
                userService.deleteUser(id);
            List<UserDto> t = userService.findUsersBy(false, "ROLE_TEACHER")
                    .stream()
                    .map(mapper::map)
                    .sorted(Comparator.comparing(UserDto::getLogin))
                    .collect(Collectors.toList());
            model.addAttribute("teacherSet", new UserListDto(t));
            return "home/teachers";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/teachers";
        }
    }

    @GetMapping("/teachers/search")
    public String getTeachers(@RequestParam(value = "username") String username, Model model) {
        try {
            if (username == null || username.trim().isEmpty()) {
                List<UserDto> t = userService.findUsersBy(false, "ROLE_TEACHER")
                        .stream()
                        .map(mapper::map)
                        .sorted(Comparator.comparing(UserDto::getLogin))
                        .collect(Collectors.toList());
                model.addAttribute("teacherSet", new UserListDto(t));
                return "home/teachers";
            }

            List<UserDto> t = userService.findInactiveTeachersByUsername(username.trim())
                    .stream()
                    .map(mapper::map)
                    .sorted(Comparator.comparing(UserDto::getLogin))
                    .collect(Collectors.toList());

            model.addAttribute("teacherSet", new UserListDto(t));

            return "home/teachers";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:teachers";
        }
    }

}
