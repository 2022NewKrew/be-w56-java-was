package controller;

import controller.annotation.RequestMapping;
import db.DataBase;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.Model;
import http.response.ModelAndView;
import lombok.extern.slf4j.Slf4j;
import model.Comment;
import model.User;
import repository.CommentRepository;
import repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class RequestUrlController {

    private final UserRepository userRepository = new UserRepository();
    private final CommentRepository commentRepository = new CommentRepository();

    @RequestMapping("/")
    public ModelAndView index(HttpRequest request, HttpResponse response) {
        List<Comment> comments = commentRepository.findAll();
        Model model = new Model();
        model.addAttribute("comments", comments);
        return new ModelAndView("/index", model);
    }

    @RequestMapping(value = "/create", method = "POST")
    public ModelAndView createUser(HttpRequest request, HttpResponse response) {
        String userId = request.getParam("userId");
        String password = request.getParam("password");
        String name = request.getParam("name");
        String email = request.getParam("email");
        User user = new User(userId, password, name, email);
        userRepository.save(user);

        log.info("user created = {}", user);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/login")
    public ModelAndView showLogin(HttpRequest request, HttpResponse response) {
        return new ModelAndView("/user/login");
    }

    @RequestMapping(value = "/login", method = "POST")
    public ModelAndView login(HttpRequest request, HttpResponse response) {

        String userId = request.getParam("userId");
        String password = request.getParam("password");
        User user = userRepository.findByUserId(userId).orElse(null);

        if (user == null || !user.getPassword().equals(password)) {
            response.addCookie("logined", "false");
            return new ModelAndView("redirect:/login-failed");
        }
        response.addCookie("userId", userId);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/login-failed")
    public ModelAndView loginFailed(HttpRequest request, HttpResponse response) {
        return new ModelAndView("/user/login_failed");
    }

    @RequestMapping("/users")
    public ModelAndView showUserList(HttpRequest request, HttpResponse response) {
        List<User> users = userRepository.findAll();
        Model model = new Model();
        model.addAttribute("users", users);
        return new ModelAndView("/user/list", model);
    }

    @RequestMapping(value = "/comment", method = "POST")
    public ModelAndView saveComment(HttpRequest request, HttpResponse response) {
        String userId = request.getCookie("userId");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }
        String comment = request.getParam("comment");
        User user = userRepository.findByUserId(userId).orElseThrow();
        Comment commentEntity = new Comment(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), user.getName(), comment);
        commentRepository.save(commentEntity);
        return new ModelAndView("redirect:/");
    }
}
