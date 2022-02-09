package webserver;

import model.Memo;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.MIME;
import webserver.http.PathInfo;

import java.io.*;
import java.nio.file.Files;
import java.util.Collection;

public class ResponseGenerator {
    public static String USERLIST_TABLE_ELEMENT_FORMAT =
            "                <tr>" +
            "                    <th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
            "                </tr>";
    public static String MEMOLIST_ELEMENT_FORMAT =
            "              <li>\n" +
            "                  <div class=\"wrap\">\n" +
            "                      <div class=\"main\">\n" +
            "                          <strong class=\"subject\">\n" +
            "                              <a href=\"./qna/show.html\">%s</a>\n" +
            "                          </strong>\n" +
            "                          <div class=\"auth-info\">\n" +
            "                              <i class=\"icon-add-comment\"></i>\n" +
            "                              <span class=\"time\">%s</span>\n" +
            "                              <a href=\"./user/profile.html\" class=\"author\">%s</a>\n" +
            "                          </div>\n" +
            "                          <div class=\"reply\" title=\"댓글\">\n" +
            "                              <i class=\"icon-reply\"></i>\n" +
            "                              <span class=\"point\">%d</span>\n" +
            "                          </div>\n" +
            "                      </div>\n" +
            "                  </div>\n" +
            "              </li>";

    private static final Logger log = LoggerFactory.getLogger(ResponseGenerator.class);

    public static HttpResponse generateStaticResponse(String path) {
        try {
            byte[] body = Files.readAllBytes(new File(PathInfo.URI_BASE + path).toPath());

            return HttpResponse.builder()
                    .status(HttpStatus.OK)
                    .contentType(MIME.parse(path))
                    .body(body)
                    .build();
        } catch (IOException e) {
            log.error(e.getMessage());
            return generateResponse500();
        }
    }

    public static HttpResponse generateLoginResponse() {
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .header("Location", PathInfo.PATH_INDEX)
                .header("Set-Cookie", "loggedin=true; Path=/")
                .build();
    }

    public static HttpResponse generateLoginFailedResponse() {
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .header("Location", PathInfo.PATH_LOGIN_FAILED)
                .header("Set-Cookie", "loggedin=false; Path=/")
                .build();
    }

    public static HttpResponse generateUserListResponse(Collection<User> userList) throws IOException{
        byte[] body = buildUserListBody(userList).getBytes();

        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .contentType(MIME.HTML.getExtension())
                .body(body)
                .build();
    }

    private static String buildUserListBody(Collection<User> userList) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(PathInfo.URI_BASE + PathInfo.PATH_USER_LIST_FILE));
        String line = br.readLine();
        while (line != null) {
            body.append(line);
            if (line.endsWith("<tbody>")) {
                line = br.readLine();
                break;
            }
            line = br.readLine();
        }

        int index = 1;
        for (User user : userList) {
            body.append(String.format(USERLIST_TABLE_ELEMENT_FORMAT,
                    index,
                    user.getUserId(),
                    user.getName(),
                    user.getEmail()
            ));
            index += 1;
        }
        while (line != null && !line.endsWith("</tbody>")) {
            line = br.readLine();
        }

        while (line != null) {
            body.append(line);
            line = br.readLine();
        }

        return body.toString();
    }

    public static HttpResponse generateIndexResponse(Collection<Memo> memoList) throws IOException{
        byte[] body = buildMemoListBody(memoList).getBytes();

        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .contentType(MIME.HTML.getExtension())
                .body(body)
                .build();
    }

    private static String buildMemoListBody(Collection<Memo> memoList) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(PathInfo.URI_BASE + PathInfo.PATH_INDEX_FILE));
        String line = br.readLine();
        while (line != null) {
            body.append(line);
            if (line.endsWith("<ul class=\"list\">")) {
                line = br.readLine();
                break;
            }
            line = br.readLine();
        }

        for (Memo memo : memoList) {
            body.append(String.format(MEMOLIST_ELEMENT_FORMAT,
                    memo.getContents(),
                    memo.getTime(),
                    memo.getWriter(),
                    memo.getMemoId()
            ));
        }
        while (line != null && !line.endsWith("</ul>")) {
            line = br.readLine();
        }

        while (line != null) {
            body.append(line);
            line = br.readLine();
        }

        return body.toString();
    }

    public static HttpResponse generateResponse302(String path) {
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .header("Location", path)
                .build();
    }

    public static HttpResponse generateResponse400(Exception e) {
        byte[] body = (HttpStatus.BAD_REQUEST + ": " + e.getMessage()).getBytes();

        return HttpResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .body(body)
                .build();
    }

    public static HttpResponse generateResponse404() {
        byte[] body = HttpStatus.NOT_FOUND.toString().getBytes();

        return HttpResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .body(body)
                .build();
    }

    public static HttpResponse generateResponse500() {
        byte[] body = HttpStatus.INTERNAL_SERVER_ERROR.toString().getBytes();

        return HttpResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body)
                .build();
    }
}
