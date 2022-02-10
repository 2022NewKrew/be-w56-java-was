package controller;

import controller.request.Request;
import controller.response.Response;
import util.HttpRequestUtils;

import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-28 028
 * Time: 오후 11:41
 */
public class UserLogoutController implements WebController{

    @Override
    public Response process(Request request) {
        String redirectPath = "/index.html";
        String cookiesHeader = request.getHeader("Set-Cookie");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookiesHeader);

        return new Response.Builder()
                .redirect()
                .header("Set-Cookie", "loggedIn=false")
                .header("Location", redirectPath)
                .build();
    }
}
