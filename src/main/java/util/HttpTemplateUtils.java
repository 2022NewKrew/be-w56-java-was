package util;

import model.User;

import java.util.Collection;
import java.util.Iterator;

public class HttpTemplateUtils {
    private static String get1UserRow(int index, User user) {
        return String.format("<tr>\n<th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n</tr>\n", index, user.getUserId(), user.getName(), user.getEmail());
    }

    public static String createUserListView(Collection<User> users) {
        StringBuilder sb = new StringBuilder();
        Iterator<User> it = users.iterator();
        int index = 1;
        while(it.hasNext()) {
            User user = it.next();
            sb.append(get1UserRow(index, user));
        }

        return sb.toString();
    }
}
