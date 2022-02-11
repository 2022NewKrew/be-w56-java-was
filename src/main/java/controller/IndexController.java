package controller;

import controller.request.Request;
import controller.response.Response;
import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ArticleService;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by melodist
 * Date: 2022-02-09 009
 * Time: 오후 8:28
 */
public class IndexController implements WebController{
    private static final String INDEX_HEADER = "./webapp/index_header";
    private static final String INDEX_FOOTER = "./webapp/index_footer";
    private static final Logger log = LoggerFactory.getLogger(UserListController.class);

    @Override
    public Response process(Request request) {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        Response response = null;

        headers.add("Content-Type", "text/html;charset=utf-8");
        try {
            String header = Files.readString(Paths.get(INDEX_HEADER));
            String footer = Files.readString(Paths.get(INDEX_FOOTER));
            String body = getBody(header, footer, ArticleService.getArticleList());

            byte[] byteBody = body.getBytes(StandardCharsets.UTF_8);
            headers.add("Content-Length", String.valueOf(byteBody.length));

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

    private String getBody(String header, String footer, List<Article> articleList) {
        StringBuilder sb = new StringBuilder(header);
        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            sb.append("\n<tr>")
                    .append("\n<th scope=\"row\">").append(i + 1).append("</th>")
                    .append("<td>").append(article.getUser().getUserId()).append("</td>")
                    .append("<td>").append(article.getCreatedDate().format(DateTimeFormatter.ISO_LOCAL_DATE)).append("</td>")
                    .append("<td>").append(URLDecoder.decode(article.getContent(), StandardCharsets.UTF_8)).append("</td>\n")
                    .append("</tr>\n");
        }
        sb.append(footer);
        return sb.toString();
    }
}
