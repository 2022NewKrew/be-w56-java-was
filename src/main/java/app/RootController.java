package app;

import app.utils.SessionUtils;
import com.google.common.net.HttpHeaders;
import webserver.WebServerConfig;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseStatus;
import webserver.servlet.HttpControllable;

public class RootController implements HttpControllable {

    public static final String path = "/";

    @Override
    public void call(HttpRequest request, HttpResponse response) {
        if (SessionUtils.hasSession(request)) {
            response.setStatus(HttpResponseStatus.FOUND);
            response.headers().set(HttpHeaders.LOCATION, WebServerConfig.ENDPOINT + "/user/list");
            return;
        }
        response.setStatus(HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaders.LOCATION, WebServerConfig.ENDPOINT + "/login.html");
    }


}
