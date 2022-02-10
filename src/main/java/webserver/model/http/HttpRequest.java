package webserver.model.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.DispatcherServlet;
import webserver.enums.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HttpMethod method;
    private final String uri;
    private final String version;
    private final String body;
    private final List<Cookie> cookies;

    public HttpRequest(HttpMethod method, String uri, String version, String body, List<Cookie> cookies) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.body = body;
        this.cookies = cookies;
    }

    public static HttpRequest of(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String requestLine = br.readLine();

        Map<String, String> httpHeaderMap = new HashMap<>();
        List<Cookie> cookies = new ArrayList<>();

        String line = br.readLine();
        while (!"".equals(line)) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            log.info(pair.getKey() + ", " + pair.getValue());
            if (pair.getKey().equals("Cookie"))
                cookies.add(new Cookie(pair.getValue()));
            else
                httpHeaderMap.put(pair.getKey(), pair.getValue());
            line = br.readLine();
        }

        String[] tokens = HttpRequestUtils.parseRequestLine(requestLine);

        String contentLengthString = httpHeaderMap.get("Content-Length");
        int contentLength = contentLengthString == null? 0: Integer.parseInt(contentLengthString);

        String body = IOUtils.readData(br, contentLength);
        return new HttpRequest(HttpMethod.valueOf(tokens[0]), tokens[1], tokens[2],
                URLDecoder.decode(body, StandardCharsets.UTF_8), cookies);
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

    public String getCookie(String key) {
        for (Cookie cookie : cookies)
            if (cookie.get(key) != null)
                return cookie.get(key);
        return null;
    }
}
