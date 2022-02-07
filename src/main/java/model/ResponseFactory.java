package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseFactory {

    private static final byte[] NOT_FOUNT_MESSAGE = "없는 페이지 입니다.".getBytes();

    private static String parseExtension(String path) {
        List<String> splitResult = List.of(path.split("\\."));
        int length = splitResult.size();
        return splitResult.get(length - 1);
    }

    public static HttpResponse getResponse(HttpRequest request, HttpStatus httpStatus) throws IOException {
        StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1.getVersion(), httpStatus.getCode(),
                httpStatus.getMessage());
        Map<String, String> headerKeyMap = new HashMap<>();
        switch (httpStatus) {
            case OK:
                File file = new File("./webapp" + request.getUrl());
                byte[] body = Files.readAllBytes(file.toPath());
                headerKeyMap.put("Content-Type", parseExtension(request.getUrl()));
                headerKeyMap.put("Content-Length", Integer.toString(body.length));
                return new HttpSuccessfulResponse(statusLine, new Header(headerKeyMap), body);
            case FOUND:
                headerKeyMap.put("Location: ", request.getUrl());
                return new HttpRedirectionResponse(statusLine, new Header(headerKeyMap));
            default:
                headerKeyMap.put("Content-Type", parseExtension(request.getUrl()));
                headerKeyMap.put("Content-Length", Integer.toString(NOT_FOUNT_MESSAGE.length));
                return new HttpClientErrorResponse(statusLine, new Header(headerKeyMap), NOT_FOUNT_MESSAGE);
        }
    }
}
