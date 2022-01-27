package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class Request {

    private String httpMethod;
    private String url;
    private Map<String, String> queryString;
    private String httpVersion;
    private final Map<String, String> headers = new HashMap<>();
    private Map<String, String> body;

    private static final Logger log = LoggerFactory.getLogger(Request.class);

    public Request(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        // request line 파싱
        parseRequestLine(br);
        // url 파싱
        parseUrl(url);
        // headers 파싱
        parseHeaders(br);
        // body 파싱
        if (headers.containsKey("Content-Length")) {
            parseBody(br, Integer.parseInt(headers.get("Content-Length")));
        }
    }

    private void parseRequestLine(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        String[] tokens = requestLine.split(" ");
        httpMethod = tokens[0];
        url = tokens[1];
        httpVersion = tokens[2];

        log.debug("request line : {}, {}, {}", httpMethod, url, httpMethod);
    }

    private void parseUrl(String url) {
        if (url.contains("?")) {
            String[] tokens = url.split("\\?");
            this.url = tokens[0];
            this.queryString = HttpRequestUtils.parseQueryString(tokens[1]);
        }
    }

    private void parseHeaders(BufferedReader br) throws IOException {
        String line = br.readLine();
        while (!(line).equals("")) {
            HttpRequestUtils.Pair header = HttpRequestUtils.parseHeader(line);
            headers.put(header.getKey(), header.getValue());

            log.debug("header {} {}", header.getKey(), header.getValue());
            line = br.readLine();
        }
    }

    private void parseBody(BufferedReader br, int contentLength) throws IOException {
        body = HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength));
    }


    public String getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getQueryString() {
        return queryString;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getBody() {
        return body;
    }
}
