package network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private final BufferedReader bufferedReader;
    private HttpMethod method;
    private String path;
    private Map<String, String> queryString;
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    public HttpRequest(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        parseRequest();
    }

    private void parseRequest() {
        try {
            parseRequestLine();
            parseHeader();
            if (!method.equals(HttpMethod.GET)) {
                parseBody();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    private void parseRequestLine() throws IOException {
        String line = bufferedReader.readLine();
        Map<String, String> tokens = HttpRequestUtils.parseRequestLine(line);
        method = HttpMethod.valueOf(tokens.get("method"));
        path = tokens.get("path");
        queryString = HttpRequestUtils.parseQueryString(tokens.get("queryString"));
    }

    private void parseHeader() throws IOException {
        String line = bufferedReader.readLine();
        while (!"".equals(line)) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headers.put(pair.getKey(), pair.getValue());
            line = bufferedReader.readLine();
        }
    }

    private void parseBody() throws IOException {
        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        body = IOUtils.readData(bufferedReader, contentLength);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryString() {
        return queryString;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        String accept = headers.get("Accept");
        return HttpRequestUtils.contentNegotation(accept);
    }
}
