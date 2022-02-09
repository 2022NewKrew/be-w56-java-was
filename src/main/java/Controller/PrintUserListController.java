package Controller;

import static webserver.http.HttpMeta.MIME_TYPE_OF_HTML;
import static webserver.http.HttpMeta.NO_SESSION;

import db.SessionStorage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import model.Session;
import model.User;
import service.GetUserListService;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class PrintUserListController implements Controller {

    private static final String TO_BE_REPLACE_STRING = "{{userList}}";
    private static final File userListFile = new File("./webapp/user/list.html");

    @Override
    public void process(HttpRequest request, HttpResponse response) throws IOException {
        if (!isLogin(request)) {
            response.redirectLoginPage();
            return;
        }

        List<User> userList = GetUserListService.getUserList();

        String body = getBody(userList);

        buildResponse(response, body);
    }

    private void buildResponse(HttpResponse response, String body) {
        response.setContentType(MIME_TYPE_OF_HTML);
        response.setContentLength(body.length());
        response.setMessage(body);
    }

    private String getBody(List<User> userList) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(userListFile.getPath()));
        String userListStr = getUserListString(userList);
        return lines.stream()
                    .map(s -> s.trim().equals(TO_BE_REPLACE_STRING) ? userListStr : s)
                    .collect(Collectors.joining("\n"));
    }

    private String getUserListString(List<User> userList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            sb.append("\n<tr>")
              .append("\n<th scope=\"row\">").append(i + 1).append("</th>")
              .append("<td>").append(user.getUserId()).append("</td>")
              .append("<td>").append(user.getName()).append("</td>")
              .append("<td>").append(user.getEmail()).append("</td>\n")
              .append("</tr>\n");
        }
        return sb.toString();
    }

    private boolean isLogin(HttpRequest request) {
        int sessionId = request.getSessionId();
        if (sessionId == NO_SESSION) {
            return false;
        }

        Session session = SessionStorage.findSessionById(sessionId);
        return session != null && !session.isExpired();
    }
}
