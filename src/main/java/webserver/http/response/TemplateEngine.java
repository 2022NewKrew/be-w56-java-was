package webserver.http.response;

import domain.model.Memo;
import domain.model.User;
import webserver.config.WebConst;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//동적 html 생성
//Todo 템플릿 엔진처럼 만들어보기
public class TemplateEngine {

    private static Map<String, HtmlCreatorMethod> handlers = new HashMap<>() {{
       put("/user/list.html", (res) -> {
           userList(res);
       });

       put("/index.html", (res) -> {
            memoList(res);
       });
    }};

    public static void createHtml(HttpResponse res) throws IOException {
        if(res.getModel().isEmpty()) {
            res.setBody(Files.readAllBytes(new File("./webapp" + res.getUrl()).toPath()));
            return;
        }

        HtmlCreatorMethod htmlCreatorMethod = handlers.get(res.getUrl());
        if(htmlCreatorMethod == null) {
            return;
        }
        htmlCreatorMethod.create(res);
    }


    private static void userList(HttpResponse res) throws IOException {
        //body 교체하는 작업 구현 (model 사용)
        StringBuilder sb = new StringBuilder();
        BufferedReader br = getBufferReader(res);

        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
            if(line.contains("<tbody>")) {
                break;
            }
        }

        List<User> users = (List<User>) res.getModel().getAttribute("users");

        users.stream().forEach(user -> {
            sb.append(String.format(
                    "<tr>\n" +
                    "<th scope=\"row\">1</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                    "                </tr>", user.getUserId(), user.getName(), user.getEmail()));
        });

        while((line = br.readLine()) != null) {
            sb.append(line);
        }
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        res.setBody(bytes);
    }

    private static void memoList(HttpResponse res) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = getBufferReader(res);

        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
            if(line.contains("<tbody>")) {
                break;
            }
        }

        List<Memo> memos = (List<Memo>) res.getModel().getAttribute("memos");
        memos.stream().forEach(memo -> {
            sb.append(String.format(
                    "              <tr>\n" +
                    "                  <th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td>\n" +
                    "              </tr>", memo.getId(), memo.getCreatedAt(), memo.getAuthor(), memo.getContent()));
        });

        while((line = br.readLine()) != null) {
            sb.append(line);
        }
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        res.setBody(bytes);
    }

    private static BufferedReader getBufferReader(HttpResponse res) throws FileNotFoundException {
        try {
            return new BufferedReader(new FileReader(WebConst.URL_PREFIX + res.getUrl()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
