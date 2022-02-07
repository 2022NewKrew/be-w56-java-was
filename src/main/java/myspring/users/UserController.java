package myspring.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotations.*;
import webserver.context.HttpSession;
import webserver.context.Model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/form")
    public String createUserForm() {
        return "user/form";
    }

    @GetMapping(path = "/{id}/form")
    public String updateUserForm(HttpSession session, @PathVariable String id, Model model) {
        Optional<UserDto> user = Optional.ofNullable((UserDto)session.getAttribute("sessionedUser"));
        if (!user.isPresent() || !user.get().getUserId().equals(id)) {
            throw new IllegalArgumentException("Invalid ID!");
        }
        model.addAttribute("user", session.getAttribute("sessionedUser"));
        return "user/updateForm";
    }

    @GetMapping(path = "/list")
    public String userList(Model model) {
        List<UserDto> users = userService.getAllUsers().stream().map(UserDto::new).collect(Collectors.toList());
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping(path = "/login")
    public String userLoginPage() {
        LOGGER.info("login req");
        return "user/login";
    }

    @PostMapping(path = "/login")
    public String userLogin(HttpSession session, UserRequest userRequest) {
        User user = UserFactory.createUser(
                userRequest.getUserId(),
                userRequest.getPassword()
        );
        User validatedUser = userService.loginUser(user);
        session.setAttribute("sessionedUser", new UserDto(validatedUser));
        return "redirect:/index";
    }

    @GetMapping(path = "/login_failed")
    public String userLoginFailed() {
        System.out.println("login_failed");
        return "user/login_failed";
    }

    @GetMapping(path = "/logout")
    public String userLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }

    @GetMapping(path = "/profile")
    public String userProfile() {
        return "user/profile";
    }

    @GetMapping(path = "/profile/{userId}")
    public String userProfile(@PathVariable String userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", new UserDto(user));
        return "user/profile";
    }

    @PostMapping(path = "/create")
    public String createUser(UserRequest userRequest) {
        userService.createUser(UserFactory.createUser(
                userRequest.getUserId(),
                userRequest.getPassword(),
                userRequest.getName(),
                userRequest.getEmail()
        ));
        return "redirect:/user/list";
    }

    @PutMapping(path = "/update")
    public String updateUser(UserRequest userRequest) {
        userService.updateUser(userRequest.getPassword(), UserFactory.createUser(
                userRequest.getUserId(),
                userRequest.getPassword(),
                userRequest.getName(),
                userRequest.getEmail()
        ));
        return "redirect:/user/list";
    }

}
