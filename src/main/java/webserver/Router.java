package webserver;

import controller.UserController;
import org.json.simple.parser.ParseException;
import webserver.request.Request;

public class Router {

    public static String roting(Request request) throws ParseException {
        String url = request.getRequestLine().getRequestUri().getUrl();
        switch (url) {
            case "/user/create":
                UserController.create(request.getRequestBody());

            return "url";
        }
        // 알맞는 url이 없으면 정적 파일
        return "static";
    }

}
