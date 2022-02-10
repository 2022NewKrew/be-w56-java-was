package controller;

import controller.request.Request;
import controller.response.Response;
import service.ArticleService;
import util.HttpRequestUtils;

import javax.ws.rs.core.AbstractMultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;
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
        AbstractMultivaluedMap<String, String> headers = new MultivaluedHashMap<>();

        String loggedIn = cookies.get("loggedIn");
        String loggedInUser = cookies.get("loggedInUser");
                
        if (loggedIn == null || loggedIn.equals("false")) {
            String redirectPath = "/user/login.html";
            headers.add("Location", redirectPath);

            return new Response.Builder()
                    .redirect()
                    .headers(headers)
                    .build();
        }

        String content = request.getBody("content");
        ArticleService.createArticle(loggedInUser, content);
        headers.add("Location", "/");
        
        return new Response.Builder()
                .redirect()
                .headers(headers)
                .build();
    }
}
