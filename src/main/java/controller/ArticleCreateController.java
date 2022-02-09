package controller;

import controller.request.Request;
import controller.response.Response;
import service.ArticleService;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-02-09 009
 * Time: 오전 11:15
 */
public class ArticleCreateController implements WebController {
    @Override
    public Response process(Request request) {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(request.getHeader("Cookie"));
        Map<String, String> headers = new HashMap<>();

        String loggedIn = cookies.get("loggedIn");
        String loggedInUser = cookies.get("loggedInUser");
                
        if (loggedIn == null || loggedIn.equals("false")) {
            String redirectPath = "/user/login.html";
            headers.put("Location", redirectPath);

            return new Response.Builder()
                    .redirect()
                    .headers(headers)
                    .build();
        }

        String content = request.getBody("content");
        ArticleService.createArticle(loggedInUser, content);
        headers.put("Location", "/index.html");
        
        return new Response.Builder()
                .redirect()
                .headers(headers)
                .build();
    }
}
