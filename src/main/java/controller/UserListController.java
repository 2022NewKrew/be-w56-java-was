package controller;

import controller.request.Request;
import controller.response.Response;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HttpRequestUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-02-07 007
 * Time: 오후 6:25
 */
public class UserListController implements WebController{
    private static final String LIST_HEADER = "./webapp/user/list_header";
    private static final String LIST_FOOTER = "./webapp/user/list_footer";
    private static final Logger log = LoggerFactory.getLogger(UserListController.class);

    @Override
    public Response process(Request request) {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(request.getHeader("Cookie"));
        Map<String, String> headers = new HashMap<>();
        Response response = null;

        String loggedIn = cookies.get("loggedIn");

        if (loggedIn == null || loggedIn.equals("false")) {
            String redirectPath = "/user/login.html";
            headers.put("Location", redirectPath);

            response = new Response.Builder()
                    .redirect()
                    .headers(headers)
                    .build();
        }

        headers.put("Content-Type", "text/html;charset=utf-8");
        try {
            String header = Files.readString(Paths.get(LIST_HEADER));
            String footer = Files.readString(Paths.get(LIST_FOOTER));
            String body = getBody(header, footer, UserService.getUserList());

            byte[] byteBody = body.getBytes(StandardCharsets.UTF_8);
            headers.put("Content-Length", String.valueOf(byteBody.length));

            response = new Response.Builder()
                    .ok()
                    .headers(headers)
                    .body(byteBody)
                    .build();
        }
        catch (IOException e) {
            log.error(e.getMessage());
        }
        return response;
    }

    private String getBody(String header, String footer, List<User> userList) {
        StringBuilder sb = new StringBuilder(header);
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            System.out.println(user);
            sb.append("\n<tr>")
                    .append("\n<th scope=\"row\">").append(i + 1).append("</th>")
                    .append("<td>").append(user.getUserId()).append("</td>")
                    .append("<td>").append(URLDecoder.decode(user.getName(), StandardCharsets.UTF_8)).append("</td>")
                    .append("<td>").append(URLDecoder.decode(user.getEmail(), StandardCharsets.UTF_8)).append("</td>\n")
                    .append("</tr>\n");
        }
        sb.append(footer);
        return sb.toString();
    }
}
