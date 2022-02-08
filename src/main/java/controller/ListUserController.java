package controller;

import jdbc.DataBase;
import model.User;
import mvcframework.RequestMethod;
import mvcframework.annotation.Controller;
import mvcframework.annotation.RequestMapping;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpSession;

import java.util.Collection;

@Controller
public class ListUserController {

    private static boolean isLogined(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public void listUsers(HttpRequest request, HttpResponse response) {
        if (!isLogined(request.getSession())) {
            response.sendRedirect("/user/login.html");
            return;
        }

        Collection<User> users = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1'>");
        for (User user : users) {
            sb.append("<tr>");
            sb.append("<td>").append(user.getUserId()).append("</td>");
            sb.append("<td>").append(user.getName()).append("</td>");
            sb.append("<td>").append(user.getEmail()).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        response.forwardBody(sb.toString());
    }

}
