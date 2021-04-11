package pl.lorenc.dodohow.services;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.lorenc.dodohow.dtos.*;
import pl.lorenc.dodohow.entities.Quiz;
import pl.lorenc.dodohow.entities.QuizClass;
import pl.lorenc.dodohow.entities.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassFacade {

    private final UserService userService;
    private final ClassService classService;
    private final QuizService quizService;
    private final DtoMapper mapper;

    @Autowired
    public ClassFacade(UserService userService, ClassService classService, QuizService quizService, DtoMapper mapper) {
        this.userService = userService;
        this.classService = classService;
        this.quizService = quizService;
        this.mapper = mapper;
    }

    public String getClasses(Model model) {
        return userService.getUserFromSession().map(u -> {
            Set<QuizClass> classes = classService.findAllByUser(u);
            if (classes == null)
                classes = new HashSet<>();
            model.addAttribute("classes", classes);
            return "course/classes";
        }).orElse("redirect:/login");
    }

    public String getUsers(Long classId, Model model) {
        if (authenticateUser(classId)) {
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
        }
        return "redirect:/classes";
    }

    public String newClass(Model model) {
        QuizClassDto quizClassDto = new QuizClassDto();
        model.addAttribute("class", quizClassDto);
        return "teacher/newClass";
    }

    public String addClass(QuizClassDto classDto) {
        return userService.getUserFromSession().map(u -> {

            classDto.setTeacherId(u.getId());
            classDto.setQuizList(Sets.newHashSet());
            classDto.setStudents(Sets.newHashSet());
            classService.save(mapper.map(classDto));

            return "redirect:/classes";

        }).orElse("redirect:/login");
    }

    public String addUserToClass(Long classId, Long userId) {

        if (authenticateUser(classId)) {
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
        }
        return "redirect:/classes";
    }

    public String getViewWithAllUsers(Long id, Model model) {

        if (authenticateUser(id)) {
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
                model.addAttribute("classId", id);

                return "teacher/allUsers";
            }).orElse("redirect:/classes");
        }
        return "redirect:/classes";
    }


    public String searchForUsers(SearchDto searchDto, Model model) {

        Optional<QuizClass> quizClass = classService.findById(searchDto.getClassId());

        if (quizClass.isEmpty())
            return "redirect:/classes";

        List<Long> ids = quizClass.get()
                .getStudents()
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());

        if (searchDto.getUsername() != null && !searchDto.getUsername().trim().isEmpty()) {
            Set<UserDto> t = userService.findUsersBy(true, "ROLE_USER", searchDto.getUsername().trim())
                    .stream()
                    .filter(u -> !ids.contains(u.getId()))
                    .map(mapper::map)
                    .collect(Collectors.toSet());
            model.addAttribute("userSet", new UserSetDto(t));
        } else {
            Set<UserDto> t = userService.findUsersBy(true, "ROLE_USER")
                    .stream()
                    .filter(u -> !ids.contains(u.getId()))
                    .map(mapper::map)
                    .collect(Collectors.toSet());
            model.addAttribute("userSet", new UserSetDto(t));
        }

        model.addAttribute("search", searchDto);
        return "teacher/allUsers";
    }

    public String getsScores(Long classId, Long userId, Model model) {


        if (authenticateUser(classId)) {
            return userService.findById(userId).map(u -> classService.findById(classId).map(c -> {
                Set<Quiz> userQuizzes = quizService.findAllByClassId(classId);

                model.addAttribute("quizzes", userQuizzes.stream()
                        .sorted(Comparator.comparing(Quiz::getNumberInClass))
                        .map(quiz -> mapper.map(quiz, u))
                        .collect(Collectors.toList())
                );
                model.addAttribute("username", u.getUsername());
                model.addAttribute("class", classService.findById(classId).map(QuizClass::getTitle).orElse(""));
                model.addAttribute("classId", classId);

                return "teacher/scores";
            }).orElse("redirect:/classes/add-users/" + classId))
                    .orElse("redirect:/classes/add-users/" + classId);

        }
        return "redirect:/classes";
    }

    public String removeUserFromClass(Long userId, Long classId) {


        Optional<QuizClass> quizClass = classService.findById(classId);

        if (quizClass.isPresent() && authenticateUser(classId)) {
            QuizClass c = quizClass.get();
            return userService.findById(userId).map(user -> {
                c.getStudents().remove(user);
                user.getClasses().remove(c);
                userService.saveRegisteredUser(user);
                classService.save(c);

                return "redirect:/classes/users/" + classId;

            }).orElse("redirect:/classes/users/" + classId);
        }
        return "redirect:/classes";
    }

    public String getClassQuizzes(Model model, Long classId) {

        if (authenticateUser(classId)) {
            return classService.findById(classId).map(c -> {
                List<QuizDto> quizzes = quizService.findAllByClassId(classId).stream()
                        .sorted(Comparator.comparing(Quiz::getNumberInClass))
                        .map(mapper::map)
                        .collect(Collectors.toList());
                model.addAttribute("class", mapper.map(c));
                model.addAttribute("quizzes", quizzes);

                return "teacher/quizzes";

            }).orElse("redirect:/classes");
        }
        return "redirect:/classes";
    }

    private boolean authenticateUser(Long classId) {
        Optional<QuizClass> quizClass = classService.findById(classId);
        if (quizClass.isPresent()) {
            Optional<User> teacherOpt = userService.getUserFromSession();
            if (teacherOpt.isPresent()) {
                QuizClass c = quizClass.get();
                User teacher = teacherOpt.get();
                return c.getTeacherId().equals(teacher.getId());
            }
        }
        return false;
    }

}
