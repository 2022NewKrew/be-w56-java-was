package model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import view.View;

public class ResponseFactory {

    private static String getContentType(String path) {
        List<String> splitResult = List.of(path.split("\\."));
        int length = splitResult.size();
        String extension = splitResult.get(length - 1);

        return Mime.getMime(extension);
    }

    public static HttpResponse getResponse(HttpRequest request, HttpStatus httpStatus) throws IOException {
        StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1.getVersion(), httpStatus.getCode(),
                httpStatus.getMessage());
        Map<String, String> headerKeyMap = new HashMap<>();
        byte[] body;

        switch (httpStatus) {
            case OK:
                body = View.get(request.getUrl());
                headerKeyMap.put("Content-Type", getContentType(request.getUrl()));
                headerKeyMap.put("Content-Length", Integer.toString(body.length));
                return new HttpSuccessfulResponse(statusLine, new Header(headerKeyMap), body);
            case FOUND:
                headerKeyMap.put("Location", "/index.html");
                return new HttpRedirectionResponse(statusLine, new Header(headerKeyMap));
            default:
                body = View.get("/notFound.html");
                headerKeyMap.put("Content-Type", getContentType(request.getUrl()));
                headerKeyMap.put("Content-Length", Integer.toString(body.length));
                return new HttpClientErrorResponse(statusLine, new Header(headerKeyMap), body);
        }
    }
}
