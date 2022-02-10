package service;

import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.PostRepository;
import util.HttpRequestUtils;
import util.HttpResponseMaker;
import web.http.request.HttpRequest;
import web.http.response.*;

import java.time.LocalDate;
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
}
