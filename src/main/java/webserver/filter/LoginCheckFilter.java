package webserver.filter;

import webserver.http.domain.CookieConst;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.util.ArrayList;
import java.util.List;

public class LoginCheckFilter extends Filter {
     public LoginCheckFilter() {
         blackList = new ArrayList<>() {{
             add("/user/list");
             add("/memo");
         }};
     }

    public boolean checkReq(HttpRequest req) {
        if (haveToCheck(req)) {
            return check(req);
        }
        return true;
    }

    @Override
    public void failResponse(HttpResponse res) {
        res.setStatusCode(302, "Found")
                .setUrl("/user/login.html")
                .forward();
    }

    private boolean haveToCheck(HttpRequest req) {
        return blackList.stream().filter(url -> req.getRequestUri().contains(url)).count() >= 1;
    }

    private boolean check(HttpRequest req) {
        if(req.getCookie().getCookie(CookieConst.LOGIN_COOKIE.toString()) == null) {
            return false;
        }
        if(!req.getCookie().getCookie(CookieConst.LOGIN_COOKIE.toString()).equals("true")) {
            return false;
        }
        return true;
    }
}
