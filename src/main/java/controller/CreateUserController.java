package controller;

import jdbc.UserDao;
import model.User;
import mvcframework.RequestMethod;
import mvcframework.annotation.Controller;
import mvcframework.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.sql.SQLException;

@Controller
public class CreateUserController {
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public void createUser(HttpRequest request, HttpResponse response) {
        User user = new User(
                request.getParam("userId"),
                request.getParam("email"),
                request.getParam("name"),
                request.getParam("email")
        );
        log.debug("user : {}", user);
        UserDao userDao = new UserDao();
        try {
            userDao.insert(user);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        response.sendRedirect("/index.html");
    }
}
