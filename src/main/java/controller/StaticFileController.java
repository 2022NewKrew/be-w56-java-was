package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import util.MapUtil;

public class StaticFileController implements Controller {

    private static final StaticFileController INSTANCE = new StaticFileController();

    public static synchronized StaticFileController getInstance() {
        return INSTANCE;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {
        return HttpResponse.ok(
                request.getUrl(),
                MapUtil.getEmptyModelMap(),
                MapUtil.getEmptyCookieMap(),
                dos);
    }
}
