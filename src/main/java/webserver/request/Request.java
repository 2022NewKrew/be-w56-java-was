package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import webserver.RequestHandler;

@Getter
public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private HttpMethod method;
    private String uri;
    private String version;
    private List<Pair> headers;

    private Request() {}

    public static Request create(InputStream in) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        Request request = new Request();

        readRequestLine(br, request);
        readHeaders(br, request);
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
        request.headers = new ArrayList<>();
        String headerLine = br.readLine();
        while (!headerLine.equals("")) {
            log.debug("http header line: {}", headerLine);
            request.headers.add(HttpRequestUtils.parseHeader(headerLine));
            headerLine = br.readLine();
        }
    }
}
