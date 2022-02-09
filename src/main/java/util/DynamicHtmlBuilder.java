package util;

import model.User;

import java.util.Collection;

public class DynamicHtmlBuilder {
    public static void build(String path, Collection collection) {
        String baseHtml = IOUtils.readHtml(path);
        String startRegex = "<tbody>";
        String endRegex = "</tbody>";
        int startIndex = baseHtml.indexOf(startRegex) + startRegex.length();
        int endIndex = baseHtml.indexOf(endRegex);

        StringBuilder contentsBuilder = new StringBuilder();
        contentsBuilder.append(baseHtml, 0, startIndex);
        contentsBuilder.append("\n");
        contentsBuilder.append(userListHtml(collection));

        contentsBuilder.append(baseHtml.substring(endIndex));

        IOUtils.writeFile("./webapp/user/dynamicList.html", contentsBuilder.toString());

    }

    private static String userListHtml(Collection<User> collection) {
        StringBuilder userList = new StringBuilder();
        int idx = 1;
        for (User user : collection) {
            userList.append("<tr>\n");
            userList.append("<th scope=\"row\">" + Integer.toString(idx++) + "</th> ");
            userList.append("<td>" + user.getUserId() + "</td> ");
            userList.append("<td>" + user.getName() + "</td> ");
            userList.append("<td>" + user.getEmail() + "</td> ");
            userList.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n");
            userList.append("</tr>\n");
        }
        return userList.toString();
    }
}
