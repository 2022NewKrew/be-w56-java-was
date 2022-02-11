package httpmodel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

    private static final String NEW_LINE = "\r\n";

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private final List<String> headers = new ArrayList<>();
    private byte[] body;
    private HttpStatus httpStatus = HttpStatus.OK;
    private Map<String, String> cookies = new HashMap<>();

    public void set200(String url, String acceptType) {
        httpStatus = HttpStatus.OK;
        try {
            byte[] responseBody = Files.readAllBytes(new File("./webapp" + url).toPath());
        headers.add("Content-Type: " + acceptType + ";charset=utf-8");
        headers.add("Content-Length: " + responseBody.length);
        body = responseBody;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set302(String location) {
        httpStatus = HttpStatus.FOUND;
        headers.add("Location: " + location);
    }

    public void addHeader(String key, String value) {
        headers.add(String.format("%s: %s", key, value));
    }

    public void addCookie(String key, String value) {
        cookies.putIfAbsent(key, value);
        log.debug("k: {}, value: {}", key, value);
    }

    public String message() {
        List<String> cookieList = new ArrayList<>();
        cookies.forEach((k, v) -> cookieList.add(k + "=" + v));
        headers.add("Set-Cookie: " + String.join(";", cookieList));
        return String.join(NEW_LINE,
            String.format("HTTP/1.1 %d %s", httpStatus.getStatus(), httpStatus.getStatusMessage()),
            String.join(NEW_LINE, headers),
            NEW_LINE
        );
    }

    public byte[] getBody() {
        return body;
    }
}
