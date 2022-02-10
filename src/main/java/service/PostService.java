package service;

import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.PostRepository;
import util.HttpRequestUtils;
import util.HttpResponseMaker;
import web.http.request.HttpRequest;
import web.http.request.HttpRequestLine;
import web.http.response.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PostService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static void addPost(String body){
        Map<String, String> bodies = HttpRequestUtils.parseBody(body);
        LocalDate createdAt = LocalDate.now();
        String author = bodies.get("author");
        String contents = bodies.get("contents");

        Post post = new Post(createdAt, author, contents);
        log.info("Insert Post: {}", post);
        PostRepository.addUser(post);
    }

    public static HttpResponse addPostRequest(HttpRequest httpRequest){
        String requestBody = httpRequest.getBodyData();
        addPost(requestBody);

        return HttpResponseMaker.redirectIndexPage(httpRequest);
    }

    public static byte[] getPostListBody() throws IOException {
        String baseHtml = new String(Files.readAllBytes(new File("./webapp/index.html").toPath()));
        String changeHtml = baseHtml.replace("{{postList}}", getPostListHtml());
        return changeHtml.getBytes(StandardCharsets.UTF_8);
    }

    private static String getPostListHtml(){
        List<Post> posts = PostRepository.findAll();

        StringBuilder sb = new StringBuilder();
        posts.forEach(post -> {
            sb.append("<tr>\n").append("<td class=\"memo-row date\">").append(post.getCreatedAt()).append("</td>\n");
            sb.append("<td class=\"memo-row author\">").append(post.getAuthor()).append("</td>\n");
            sb.append("<td class=\"memo-row contents\">").append(post.getContents()).append("</td>\n").append("</tr>\n");
        });

        return sb.toString();
    }
}
