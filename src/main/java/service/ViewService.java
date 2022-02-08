package service;

import db.DataBase;
import model.User;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewService {

    public static byte[] getUserListBody(String url) throws IOException {
        String baseHtml = new String(Files.readAllBytes(new File("./webapp" + url).toPath()));
        String userListView = baseHtml.replace("{{userList}}", getUserListHtml());

        return userListView.getBytes(StandardCharsets.UTF_8);
    }

    private static String getUserListHtml() {
        List<User> users = new ArrayList<>(DataBase.findAll());

        AtomicInteger atomicInteger = new AtomicInteger();
        StringBuilder sb = new StringBuilder();
        users.forEach(user -> {
            sb.append("<tr>\n").append("<th scope=\"row\">").append(atomicInteger.incrementAndGet()).append("</th>\n");
            sb.append("<td>").append(user.getUserId()).append("</td>\n");
            sb.append("<td>").append(user.getName()).append("</td>\n");
            sb.append("<td>").append(user.getEmail()).append("</td>\n");
            sb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n").append("</tr>\n");
        });

        return sb.toString();
    }

}
