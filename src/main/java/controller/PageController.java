package controller;

import static webserver.AppConfig.appConfig;

import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import util.HttpResponseUtils;

public class PageController extends AbstractController {

    private final String ERROR_POST = "[ERROR] post 요청을 할 수 없습니다.";

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        String path = httpRequest.getPath();
        if (isHome(path)) {
            path = appConfig.HOME;
        }
        httpResponse.set200(path, HttpResponseUtils.acceptType(path));
    }

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        throw new IllegalArgumentException(ERROR_POST);
    }

    public boolean isHome(String path) {
        return path.equals("/");
    }
}
