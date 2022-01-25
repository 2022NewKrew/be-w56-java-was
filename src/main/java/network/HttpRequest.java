package network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private final BufferedReader bufferedReader;
    private HttpMethod method;
    private String path;
    private String queryString;
    private String version;
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    public HttpRequest(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        parseRequest();
    }

    private void parseRequest() {
        try {
            String line = bufferedReader.readLine();
            parseRequestLine(line);

            line = bufferedReader.readLine();
            while (!"".equals(line)) {
                parseHeader(line);
                line = bufferedReader.readLine();
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    private void parseRequestLine(String line) {
        Map<String, String> tokens = HttpRequestUtils.parseRequestLine(line);
        this.method = HttpMethod.valueOf(tokens.get("method"));
        this.path = tokens.get("path");
        this.queryString = tokens.get("queryString");
        this.version = tokens.get("version");
    }

    private void parseHeader(String line) {
        HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
        headers.put(pair.getKey(), pair.getValue());
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getContentType() {
        String accept = headers.get("Accept");
        return HttpRequestUtils.contentNegotation(accept);
    }
}
