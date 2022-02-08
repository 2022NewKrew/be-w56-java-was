package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestStartLine {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestStartLine.class);

    private final String method;
    private final String url;
    private final String protocol;

    private HttpRequestStartLine(String method, String url, String protocol) {
        this.method = method;
        this.url = url;
        this.protocol = protocol;
    }

    public static HttpRequestStartLine of(BufferedReader br) {
        String method = "";
        String url = "";
        String protocol = "";

        try {
            String startLine = br.readLine();
            if (startLine == null || startLine.equals("")) {
                throw new IllegalArgumentException("[ERROR] Request 헤더가 없습니다.");
            }

            String[] startLineItems = startLine.split(" ");
            method = startLineItems[0].toUpperCase();
            url = startLineItems[1];
            protocol = startLineItems[2];
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }

        return new HttpRequestStartLine(method, url, protocol);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean hasValue() {
        return !(method.isEmpty() || url.isEmpty() || protocol.isEmpty());
    }
}
