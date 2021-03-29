package pl.lorenc.dodohow.services;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lorenc.dodohow.dtos.QuizClassDto;
import pl.lorenc.dodohow.dtos.SearchDto;
import pl.lorenc.dodohow.dtos.UserDto;
import pl.lorenc.dodohow.dtos.UserSetDto;
import pl.lorenc.dodohow.entities.QuizClass;
import pl.lorenc.dodohow.entities.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClassFacade {

    private UserService userService;
    private ClassService classService;
    private DtoMapper mapper;

    @Autowired
    public ClassFacade(UserService userService, ClassService classService, DtoMapper mapper) {
        this.userService = userService;
        this.classService = classService;
        this.mapper = mapper;
    }

    public String getClasses(Model model) {
        try {
            return userService.getUserFromSession().map(u -> {
                Set<QuizClass> classes = classService.findAllByUser(u);
                if (classes == null)
                    classes = new HashSet<>();
                model.addAttribute("classes", classes);
                return "course/classes";

            }).orElse("redirect:/login");

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    public String getUsers(Long classId, Model model) {
        try {

            return classService.findById(classId).map(c -> {

                QuizClassDto quizClassDto = mapper.map(c);
                Set<UserDto> users = userService.findAllById(c.getStudents().stream().map(User::getId).collect(Collectors.toList()))
                        .stream()
                        .map(mapper::map)
                        .collect(Collectors.toSet());

                UserSetDto userSetDto = new UserSetDto(users);

                model.addAttribute("class", quizClassDto);
                model.addAttribute("userSet", userSetDto);

                return "teacher/users";

            }).orElse("redirect:/classes");

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    public String newClass(Model model) {
        try {
            QuizClassDto quizClassDto = new QuizClassDto();
            model.addAttribute("class", quizClassDto);
            return "teacher/newClass";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    public String addClass(QuizClassDto classDto) {
        try {
            return userService.getUserFromSession().map(u -> {

                classDto.setTeacherId(u.getId());
                classDto.setQuizList(Sets.newHashSet());
                classDto.setStudents(Sets.newHashSet());
                classService.save(mapper.map(classDto));

                return "redirect:/classes";

            }).orElse("redirect:/login");

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    public String addUserToClass(Long classId, Long userId) {
        try {

            return userService.findByIdWithClasses(userId).map(user ->
                    classService.findById(classId).map(c -> {
                        c.getStudents().add(user);
                        if (user.getClasses() == null) {
                            user.setClasses(new HashSet<>());
                        }
                        user.getClasses().add(c);
                        classService.save(c);
                        userService.saveRegisteredUser(user);
                        return "redirect:/classes/add-users/" + classId;
                    }).orElse("redirect:/classes")).orElse("redirect:/classes");

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }

    public String getViewWithAllUsers(Long id, Model model) {
        try {

            return classService.findById(id).map(c -> {

                List<Long> ids = c.getStudents()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList());

                Set<UserDto> users = userService.findUsersBy(true, "ROLE_USER").stream()
                        .filter(u -> !ids.contains(u.getId()))
                        .map(mapper::map)
                        .collect(Collectors.toSet());

                SearchDto searchDto = new SearchDto();
                searchDto.setClassId(c.getId());

                model.addAttribute("search", searchDto);
                model.addAttribute("userSet", new UserSetDto(users));

                return "teacher/allUsers";
            }).orElse("redirect:/classes");

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }


    public String searchForUsers(SearchDto searchDto, Model model) {
        try {
            if (searchDto.getUsername() != null && !searchDto.getUsername().trim().isEmpty()) {
                Set<UserDto> t = userService.findUsersBy(true, "ROLE_USER", searchDto.getUsername().trim())
                        .stream()
                        .map(mapper::map)
                        .collect(Collectors.toSet());
                model.addAttribute("userSet", new UserSetDto(t));
            } else {
                Set<UserDto> t = userService.findUsersBy(true, "ROLE_USER")
                        .stream()
                        .map(mapper::map)
                        .collect(Collectors.toSet());
                model.addAttribute("userSet", new UserSetDto(t));
            }

            model.addAttribute("search", searchDto);

            return "teacher/allUsers";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/classes";
        }
    }
}
