package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestStartLine {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestStartLine.class);

    private final HttpRequestMethod method;
    private final String url;
    private final String protocol;

    private HttpRequestStartLine(HttpRequestMethod method, String url, String protocol) {
        this.method = method;
        this.url = url;
        this.protocol = protocol;
    }

    public static HttpRequestStartLine of(BufferedReader br) {
        HttpRequestMethod method = HttpRequestMethod.GET;
        String url = "";
        String protocol = "";

        try {
            String startLine = br.readLine();
            if (startLine == null || startLine.equals("")) {
                throw new IllegalArgumentException("[ERROR] Request 헤더가 없습니다.");
            }

            String[] startLineItems = startLine.split(" ");
            method = HttpRequestMethod.valueOf(startLineItems[0].toUpperCase());
            url = startLineItems[1];
            protocol = startLineItems[2];
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return new HttpRequestStartLine(method, url, protocol);
    }

    public HttpRequestMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean hasValue() {
        return !(url.isEmpty() || protocol.isEmpty());
    }
}
