package webserver.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

@Slf4j
@Getter
public class Request {

    private HttpMethod method;
    private String uri;
    private String version;
    private List<Pair> headers;

    private Request() {}

    public static Request create(String requestLine) throws Exception{
        log.info("request line : {}", requestLine);
        Request request = new Request();
        request.method = HttpRequestUtils.parseHttpMethod(requestLine);
        request.uri = HttpRequestUtils.parseUri(requestLine);
        request.version = HttpRequestUtils.parseHttpVersion(requestLine);
        request.headers = new ArrayList<>();
        return request;
    }

    public void addHeader(String headerLine) {
        headers.add(HttpRequestUtils.parseHeader(headerLine));
    }
}
