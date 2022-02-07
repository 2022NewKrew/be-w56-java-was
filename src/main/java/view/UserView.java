package view;


import model.User;

import java.io.*;
import java.util.Collection;

public class UserView {

    public static String getAllUserAsHTML(Collection<User> allUser) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("webapp/user/list.html")));
        StringBuilder html = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            html.append(line);
            if (line.trim().equals("<tbody class=\"user\">")) {
                html.append(convertUserToHTML(allUser));
            }
            line = br.readLine();
        }
        return html.toString();
    }

    private static String convertUserToHTML(Collection<User> allUser) {
        StringBuilder allUsers = new StringBuilder();
        int index = 0;

        for (User user : allUser) {
            allUsers.append("<tr>");
            allUsers.append("<th scope=\"row\">" + index++ + "</th>");
            allUsers.append("<td>" + user.getUserId() + "</td>");
            allUsers.append("<td>" + user.getName() + " </td>");
            allUsers.append("<td>" + user.getEmail() + "</td>");
            allUsers.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
            allUsers.append("</tr>");
        }

        return allUsers.toString();
    }
}
