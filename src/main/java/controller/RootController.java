package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import util.MapUtil;

public class RootController implements Controller {

    private static final RootController INSTANCE = new RootController();

    public static synchronized RootController getInstance() {
        return INSTANCE;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {
        return HttpResponse.found(
                "/index.html",
                MapUtil.getEmptyCookieMap(),
                dos
        );
    }
}
