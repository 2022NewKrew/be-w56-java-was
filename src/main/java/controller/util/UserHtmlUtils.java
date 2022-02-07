package controller.util;

import domain.user.dto.UserInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class UserHtmlUtils {

    private static final String header;
    private static final String footer;
    private static final String containerPrefix = "<div class=\"container\" id=\"main\">\n"
        + "<div class=\"col-md-10 col-md-offset-1\">\n"
        + "<div class=\"panel panel-default\">\n"
        + "<table class=\"table table-hover\">\n"
        + "<thead>\n"
        + "<tr>\n"
        + "<th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th>\n"
        + "</tr>\n"
        + "</thead>\n"
        + "<tbody>\n";
    private static final String containerPostFix = "</tbody>\n"
        + "</table>\n"
        + "</div>\n"
        + "</div>\n"
        + "</div>";

    static {
        header = readFromInputStream(
            UserHtmlUtils.class.getClassLoader().getResourceAsStream("templates/header.html"));
        footer = readFromInputStream(
            UserHtmlUtils.class.getClassLoader().getResourceAsStream("templates/footer.html"));
    }

    private static String readFromInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String generateUsersHtml(List<UserInfo> userInfos) {
        StringBuilder stringBuilder = new StringBuilder(header);
        stringBuilder.append(containerPrefix);

        for (int i = 0; i < userInfos.size(); i++) {
            stringBuilder.append("<tr>")
                .append("<th scope=\"row\">").append(i).append("</th>")
                .append("<td>").append(userInfos.get(i).getUserId()).append("</td>")
                .append("<td>").append(userInfos.get(i).getName()).append("</td>")
                .append("<td>").append(userInfos.get(i).getEmail()).append("</td>")
                .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                .append("</tr>");
        }

        stringBuilder.append(containerPostFix)
            .append(footer);

        return stringBuilder.toString();
    }
}
