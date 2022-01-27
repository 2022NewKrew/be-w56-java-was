package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.enums.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final HttpMethod method;
    private final String uri;
    private final String version;
    private final String body;

    public HttpRequest(HttpMethod method, String uri, String version, String body) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.body = body;
    }

    public static HttpRequest of(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String requestLine = br.readLine();

        Map<String, String> httpHeaderMap = new HashMap<>();

        String line = br.readLine();
        while (!"".equals(line)) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            log.info(pair.getKey() + ", " + pair.getValue());
            httpHeaderMap.put(pair.getKey(), pair.getValue());
            line = br.readLine();
        }

        String[] tokens = HttpRequestUtils.parseRequestLine(requestLine);
        log.info(HttpMethod.valueOf(tokens[0]).name());
        String contentLengthString = httpHeaderMap.get("Content-Length");
        int contentLength = contentLengthString == null? 0: Integer.parseInt(contentLengthString);

        String body = IOUtils.readData(br, contentLength);
        return new HttpRequest(HttpMethod.valueOf(tokens[0]), tokens[1], tokens[2], body);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getVersion() {
        return version;
    }

    public String getBody() {
        return body;
    }
}
