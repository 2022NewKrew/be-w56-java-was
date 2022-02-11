package httpmodel;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class HttpRequest {

    private final String ERROR_WRONG_REQUEST = "[ERROR] 잘못된 Request 입니다.";
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final String method;
    private final String path;
    private final Map<String, String> querys;
    private final Map<String, String> headers;
    private final Map<String, String> body;
    private final Map<String, String> cookies;

    public HttpRequest(BufferedReader bufferedReader) {
        try {
            String line = bufferedReader.readLine();
            log.debug(line);
            String[] splited = line.split(" ");
            String[] splitedUri = splited[1].split("\\?");
            this.method = splited[0];
            this.path = splitedUri[0];
            this.querys = HttpRequestUtils.getQuerys(splitedUri);
            this.headers = HttpRequestUtils.getHeaders(bufferedReader);
            this.body = HttpRequestUtils.getBody(bufferedReader, headers, method);
            this.cookies = HttpRequestUtils.parseCookies(headers.getOrDefault("Cookie", null));
            cookies.forEach((k, v) -> log.debug("key: {}, value: {}", k, v));
        } catch (Exception e) {
            log.error("Request Error : {}", e.getMessage());
            throw new IllegalArgumentException(ERROR_WRONG_REQUEST);
        }
    }

    public boolean isPathFile() {
        return path.endsWith(".html")
            || path.endsWith(".css")
            || path.endsWith(".js")
            || path.endsWith(".ttf")
            || path.endsWith(".ico")
            || path.endsWith(".woff")
            || path.endsWith(".woff2");
    }

    public boolean isPathMatch(String target) {
        return path.equals(target);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQuerys() {
        return querys;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }
}
