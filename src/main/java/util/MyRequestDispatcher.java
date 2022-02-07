package util;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class MyRequestDispatcher {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private String viewPath;

    public MyRequestDispatcher(String viewPath) {
        this.viewPath = viewPath;
    }

    public void forward(MyHttpRequest request, MyHttpResponse response) throws IOException {

        // TODO : 패턴을 쓰면 좋을것 같은데.. 어떻게??
        // redirect
        if (viewPath.indexOf("redirect:") == 0) {
            String redirect[] = viewPath.split("redirect:");

            response302(response, redirect[1]);
            log.debug("redirectURI : {}", request.getHost() + redirect[1]);
            return;
        }

        MyAttribute attribute = request.getAttribute();

        if (attribute.getInfo().size() == 0) {
            response.setBody(viewPath);
        } else {
            StringBuilder html = new StringBuilder();

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(viewPath), StandardCharsets.UTF_8));
                String str;

                while ((str = in.readLine()) != null) {
                    html.append(str);
                }
                in.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }

            String body = html.toString();

            // TODO : 현재는 users만 되고 파싱 만들어야함
            for (String key : attribute.getInfo().keySet()) {
                if ("users".equals(key)) {
                    List<User> users = (List<User>) attribute.getInfo().get(key);
                    StringBuilder sb = new StringBuilder();
                    int inedx = 0;
                    for (User user : users) {
                        sb.append("<tr>")
                                .append("<th scope=\"row\">").append(++inedx).append("</th>")
                                .append("<td>").append(user.getUserId()).append("</td>")
                                .append("<td>").append(user.getName()).append("</td>")
                                .append("<td>").append(user.getEmail()).append("</td>")
                                .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                                .append("</tr>");
                    }

                    body = body.replace("{{" + key + "}}", sb.toString());
                }
            }

            System.out.println(body);
            response.setBody(body.getBytes(StandardCharsets.UTF_8));
        }

        if (response.getStatus() == MyHttpResponseStatus.OK) {
            response200Header(request, response);
            responseBody(response);
        }
    }

    private void response200Header(MyHttpRequest request, MyHttpResponse response) {

        DataOutputStream dos = response.getDos();
        String accept = request.getAccept();
        int lengthOfBodyContent = response.getBody().length;

        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + accept + ";charset=utf-8\r\n");
            dos.writeBytes("Connection: close\r\n");

            if (!response.getCookie().isEmpty()) {
                log.debug("Set-Cookie: {}", response.getCookie());
                dos.writeBytes("Set-Cookie: " + response.getCookie());
                dos.writeBytes("\r\n");
            }

            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302(MyHttpResponse response, String location) {
        DataOutputStream dos = response.getDos();
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location);
            dos.writeBytes("\r\n");

            if (!response.getCookie().isEmpty()) {
                log.debug("Set-Cookie: {}", response.getCookie());
                dos.writeBytes("Set-Cookie: " + response.getCookie());
                dos.writeBytes("\r\n");
            }

            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(MyHttpResponse response) {
        DataOutputStream dos = response.getDos();
        byte[] body = response.getBody();

        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
