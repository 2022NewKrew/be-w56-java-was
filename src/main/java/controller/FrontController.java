package controller;

import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import java.io.IOException;
import util.FileConverter;

public class FrontController extends AbstractController {

    private static final String INDEX = "/";

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        try {
            byte[] responseBody = getResponseBody(httpRequest);
            httpResponse.set200OK(httpRequest, responseBody);
        } catch (NullPointerException exception) {
            throw new IllegalArgumentException("[ERROR] 해당파일은 존재하지 않습니다.");
        }
    }

    private byte[] getResponseBody(HttpRequest request) throws IOException {
        String uri = request.getUri();
        if (INDEX.equals(uri)) {
            return FileConverter.fileToString("/index.html");
        }
        return FileConverter.fileToString(uri);
    }

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        throw new IllegalArgumentException("[ERROR] 파일은 post 요청할 수 없습니다.");
    }
}
