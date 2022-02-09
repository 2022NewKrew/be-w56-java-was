package controller.util;

import domain.memo.dto.MemoInfo;
import domain.user.dto.UserInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public final class HtmlResponseUtils {

    private static final String HOME_TEMPLATE;
    private static final String HOME_TARGET = "{{memoInfos}}";

    private static final String USERS_HTML_TEMPLATE;
    private static final String USERS_HTML_TARGET = "{{userInfos}}";

    static {
        HOME_TEMPLATE = readFromInputStream(HtmlResponseUtils.class.getClassLoader()
            .getResourceAsStream("templates/index.html"));
        USERS_HTML_TEMPLATE = readFromInputStream(HtmlResponseUtils.class.getClassLoader()
            .getResourceAsStream("templates/user/list.html"));
    }

    private HtmlResponseUtils() {
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
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < userInfos.size(); i++) {
            stringBuilder.append("<tr>")
                .append("<th scope=\"row\">").append(i + 1).append("</th>")
                .append("<td>").append(userInfos.get(i).getUserId()).append("</td>")
                .append("<td>").append(userInfos.get(i).getName()).append("</td>")
                .append("<td>").append(userInfos.get(i).getEmail()).append("</td>")
                .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                .append("</tr>");
        }

        return USERS_HTML_TEMPLATE.replace(USERS_HTML_TARGET, stringBuilder.toString());
    }

    public static String generateMemosHtml(List<MemoInfo> memoInfos) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < memoInfos.size(); i++) {
            stringBuilder.append("<tr>")
                .append("<td>").append(memoInfos.get(i).getCreatedAt()).append("</td>")
                .append("<td>").append(memoInfos.get(i).getAuthor()).append("</td>")
                .append("<td>").append(memoInfos.get(i).getContent()).append("</td>")
                .append("</tr>");
        }

        return HOME_TEMPLATE.replace(HOME_TARGET, stringBuilder.toString());
    }
}
