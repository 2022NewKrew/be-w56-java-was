package webserver.request;

import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

@Getter
public class Request {

    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private HttpMethod method;
    private String uri;
    private String version;
    private Map<String, String> headers;
    private Map<String, String> body;

    private Request() {
    }

    public static Request create(InputStream in) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        Request request = new Request();

        readRequestLine(br, request);
        readHeaders(br, request);
        readBody(br, request);
        return request;
    }

    private static void readRequestLine(BufferedReader br, Request request) throws Exception {
        String requestLine = br.readLine();
        log.debug("http request line: {}", requestLine);
        request.method = HttpRequestUtils.parseHttpMethod(requestLine);
        request.uri = HttpRequestUtils.parseUri(requestLine);
        request.version = HttpRequestUtils.parseHttpVersion(requestLine);
    }

    private static void readHeaders(BufferedReader br, Request request) throws Exception {
        request.headers = Maps.newHashMap();
        String headerLine = br.readLine();
        while (!headerLine.equals("")) {
            log.debug("http header line: {}", headerLine);
            Pair header = HttpRequestUtils.parseHeader(headerLine);
            request.headers.put(header.getKey(), header.getValue());
            headerLine = br.readLine();
        }
    }

    private static void readBody(BufferedReader br, Request request) throws IOException {
        int len = Integer.parseInt(request.headers.getOrDefault("Content-Length", "0"));
        if (len > 0) {
            char[] buf = new char[len];
            br.read(buf);
            request.body = HttpRequestUtils.parseQueryString(String.valueOf(buf));
            log.debug("http request body: {}", request.body);
        }
    }

    public String getQuery(String key) {
        return body.get(key);
    }
}
