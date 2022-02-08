package controller;

import annotation.Auth;
import annotation.RequestMapping;
import domain.Email;
import domain.Name;
import domain.Password;
import domain.UserId;
import dto.UserDto;
import exception.CreateUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.RequestHandler;
import webserver.html.HtmlBuilder;
import webserver.html.Message;
import webserver.html.Model;
import webserver.http.HttpMethod;
import webserver.http.HttpStatus;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.ResponseBuilder;
import webserver.response.ResponseException;

import javax.annotation.Nullable;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class UserController extends Controller {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final UserController userController = new UserController();

    private final UserService userService = UserService.getInstance();

    private UserController() {
    }

    public static UserController getInstance() {
        return userController;
    }

    @Override
    public Response view(Request request, String userUrl) throws IOException {
        log.debug("user url : {}", userUrl);
        return super.view(request, userUrl);
    }

    @RequestMapping(url = "/create", method = HttpMethod.POST)
    private Response createUser(Map<String, String> parameters) throws IOException {
        try {
            UserId userId = new UserId(parameters.get("userId"));
            Password password = new Password(parameters.get("password"));
            Name name = new Name(parameters.get("name"));
            Email email = new Email(parameters.get("email"));
            userService.save(new UserDto(userId, password, name, email, LocalDateTime.now()));
        } catch (NullPointerException | CreateUserException e) {
            log.error("create user error : {}", e.getMessage());
            // 400 Bad Request
            return ResponseException.errorResponse(new Message(e.getMessage(), "/user/form.html"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseBuilder().setHttpStatus(HttpStatus.FOUND)
                .addHeader("location", "/")
                .build();
    }

    @RequestMapping(url = "/login", method = HttpMethod.POST)
    private Response login(Map<String, String> parameters) throws IOException {
        UserDto user = new UserDto();
        try {
            UserId userId = new UserId(parameters.get("userId"));
            Password password = new Password(parameters.get("password"));
            user.setUserId(userId);
            user.setPassword(password);
        } catch (NullPointerException e) {
            log.error("create user error : {}", e.getMessage());
            return ResponseException.errorResponse(new Message(e.getMessage(), "/user/login.html"), HttpStatus.BAD_REQUEST);
        }
        if (!userService.validateUser(user)) {
            return new ResponseBuilder().setHttpStatus(HttpStatus.FOUND)
                    .addHeader("location", "/user/login_failed.html")
                    .addHeader("Set-Cookie", "userId= ; Path=/")
                    .build();
        }
        return new ResponseBuilder().setHttpStatus(HttpStatus.FOUND)
                .addHeader("location", "/")
                .addHeader("Set-Cookie", "userId=" + parameters.get("userId") + "; Path=/")
                .build();
    }

    @Auth
    @RequestMapping(url = "/list", method = HttpMethod.GET)
    private Response userList(@Nullable Map<String, String> parameters) throws IOException {
        Model model = new Model();
        model.setAttribute("users", userService.findAll());
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        String html = htmlBuilder.build("./webapp/user/list.html", model);
        byte[] body = html.getBytes();
        return new ResponseBuilder().setHttpStatus(HttpStatus.OK)
                .setContent("text/html", body)
                .setBody(body)
                .build();
    }
}
