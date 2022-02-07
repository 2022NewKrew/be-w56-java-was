package Controller;

import static webserver.http.HttpMeta.MIME_TYPE_OF_HTML;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import model.User;
import service.GetUserListService;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class PrintUserListController implements Controller {

    private static final String HEADER = "./webapp/list_header";
    private static final String FOOTER = "./webapp/list_footer";

    @Override
    public void process(HttpRequest request, HttpResponse response) throws IOException {
        if (!request.isLogin()) {
            response.redirectLoginPage();
            return;
        }

        List<User> userList = GetUserListService.getUserList();
        String header = Files.readString(Paths.get(HEADER));
        String footer = Files.readString(Paths.get(FOOTER));

        String body = getBody(userList, header, footer);

        buildResponse(response, body);
    }

    private void buildResponse(HttpResponse response, String body) {
        response.setContentType(MIME_TYPE_OF_HTML);
        response.setContentLength(body.length());
        response.setMessage(body);
    }

    private String getBody(List<User> userList, String header, String footer) {
        StringBuilder sb = new StringBuilder(header);
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            sb.append("\n<tr>")
              .append("\n<th scope=\"row\">").append(i + 1).append("</th>")
              .append("<td>").append(user.getUserId()).append("</td>")
              .append("<td>").append(user.getName()).append("</td>")
              .append("<td>").append(user.getEmail()).append("</td>\n")
              .append("</tr>\n");
        }
        sb.append(footer);
        return sb.toString();
    }
}
