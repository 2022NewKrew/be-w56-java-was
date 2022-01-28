package controller;

import controller.request.Request;
import controller.response.Response;
import model.User;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-28 028
 * Time: 오후 11:41
 */
public class UserLogoutController implements WebController{

    @Override
    public Response process(Request request) {
        Map<String, String> headers = new HashMap<>();

        String redirectPath = "/index.html";
        String cookiesHeader = request.getHeader("Set-Cookie");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookiesHeader);
        String logined = cookies.get("logined");

        headers.put("Set-Cookie", "logined=false");
        headers.put("Location", redirectPath);

        return new Response.Builder()
                .redirect()
                .headers(headers)
                .build();
    }
}
