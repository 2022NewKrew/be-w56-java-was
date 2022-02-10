package controller;

import dto.UserLoginRequest;
import dto.UserSignupRequest;
import exception.AlreadyLoginException;
import exception.UserNotFoundException;
import http.GetMapping;
import http.HttpHeader;
import http.HttpResponse;
import http.HttpSession;
import http.PostMapping;
import java.util.List;
import model.User;
import service.UserService;
import webserver.Model;
import webserver.SessionStore;

@Controller
public class UserController {

    private final UserService userService = new UserService();

    @PostMapping("/user")
    public void signup(UserSignupRequest request, HttpResponse httpResponse) {
        userService.signup(request);
        httpResponse.sendRedirect("/index.html");
    }

    @PostMapping("/user/login")
    public void login(UserLoginRequest request, HttpSession httpSession,
        HttpResponse httpResponse) {
        try {
            User user = userService.login(request);
            addSession(httpSession, user);
            httpResponse.putHeader(HttpHeader.SET_COOKIE,
                "JSESSIONID=" + httpSession.getId() + "; Path=/; HttpOnly");
            httpResponse.sendRedirect("/index.html");
        } catch (UserNotFoundException e) {
            httpResponse.sendRedirect("/user/login_failed.html");
        } catch (AlreadyLoginException e) {
            httpResponse.sendRedirect("/index.html");
        }
    }

    @GetMapping("/user/list")
    public void list(HttpSession httpSession, HttpResponse httpResponse, Model model) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            httpResponse.sendRedirect("/user/login.html");
            return;
        }
        List<User> users = userService.findAll();
        model.setAttribute("users", users);
        httpResponse.ok("/user/list.html");
    }

    private void addSession(HttpSession httpSession, User user) {
        String sessionId = httpSession.getId();
        if (SessionStore.find(sessionId).isPresent()) {
            throw new AlreadyLoginException();
        }
        httpSession.setAttribute("user", user);
        SessionStore.add(sessionId, httpSession);
    }
}
