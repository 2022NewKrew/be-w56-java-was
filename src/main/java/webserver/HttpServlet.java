package webserver;

import util.http.HttpRequest;
import util.http.HttpResponse;

public interface HttpServlet {
    void service(HttpRequest httpRequest, HttpResponse httpResponse);
}
