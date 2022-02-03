package util;

import db.DataBase;
import model.User;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class DataBaseUtils {

    public static String setUserTable() {
        StringBuilder users = new StringBuilder();
        AtomicInteger index = new AtomicInteger(1);
        DataBase.findAll().forEach(user -> {
            users.append("<tr>\n");
            users.append(String.format("<th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n",
                    index.getAndIncrement(), user.getUserId(), user.getName(), user.getEmail()));
            users.append("</tr>\n");
        });
        return users.toString();
    }
}
