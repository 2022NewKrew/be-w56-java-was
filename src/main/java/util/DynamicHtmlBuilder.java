package util;

import model.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class DynamicHtmlBuilder {
    public static void build(String path, Collection<User> collection) {
        String baseHtml = IOUtils.readHtml(path);
        String startRegex = "<tbody>";
        String endRegex = "</tbody>";
        int startIndex = baseHtml.indexOf(startRegex) + startRegex.length();
        int endIndex = baseHtml.indexOf(endRegex);

        StringBuilder contentsBuilder = new StringBuilder();
        contentsBuilder.append(baseHtml, 0, startIndex);
        contentsBuilder.append("\n");
        int idx = 1;
        for (User user : collection) {
            contentsBuilder.append("<tr>\n");
            contentsBuilder.append("<th scope=\"row\">" + Integer.toString(idx++) + "</th> ");
            contentsBuilder.append("<td>" + user.getUserId() + "</td> ");
            contentsBuilder.append("<td>" + user.getName() + "</td> ");
            contentsBuilder.append("<td>" + user.getEmail() + "</td> ");
            contentsBuilder.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n");
            contentsBuilder.append("</tr>\n");
        }

        contentsBuilder.append(baseHtml.substring(endIndex));

        File file = new File("./webapp/user/dynamicList.html");
        byte[] bytes = contentsBuilder.toString().getBytes(StandardCharsets.UTF_8);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
