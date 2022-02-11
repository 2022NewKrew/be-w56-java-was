package http;

import collections.RequestBody;
import collections.RequestHeaders;
import collections.RequestStartLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import static util.HttpRequestUtils.parseHeader;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private RequestStartLine requestStartLine;
    private RequestHeaders requestHeaders;
    private RequestBody requestBody;

    public static HttpRequest of(BufferedReader br, String line) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.parseStartLine(line);
        httpRequest.parseHeaders(br);
        httpRequest.parseBody(br);

        return httpRequest;
    }

    private void parseStartLine(String line) {
        log.debug("REQ: {}", line);
        String[] tokens = line.split(" ");
        this.requestStartLine = new RequestStartLine(new HashMap<>() {{
            put("Method", tokens[0]);
            put("Path", tokens[1]);
            put("Protocol", tokens[2]);
        }});
    }

    private void parseHeaders(BufferedReader br) throws IOException {
        String line;
        line = br.readLine();
        var tempRequestHeaders = new HashMap<String, String>();
        while (line != null && !line.equals("")) {
            HttpRequestUtils.Pair pair = parseHeader(line);
            tempRequestHeaders.put(pair.getKey(), pair.getValue());
            log.debug("     {}", line);

            line = br.readLine();
        }
        this.requestHeaders = new RequestHeaders(tempRequestHeaders);
    }

    private void parseBody(BufferedReader br) throws IOException {
        Set<String> headerKeys = this.requestHeaders.getHeaderKeys();
        if (headerKeys.contains("Content-Length")) {
            int contentLength = Integer.parseInt(this.requestHeaders.getHeader("Content-Length"));
            String content = IOUtils.readData(br, contentLength);
            this.requestBody = new RequestBody(content);
        }
    }

    public RequestStartLine getRequestStartLine() {
        return requestStartLine;
    }

    public RequestHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public String getParameter(String key) {
        return requestHeaders.getHeader(key);
    }
}
