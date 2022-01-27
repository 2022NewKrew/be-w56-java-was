package http.request;

import http.util.HttpRequestUtils;

import java.util.Objects;

public class HttpRequestStartLine {
    private static final String ROOT_URI = "/";
    private static final String ROOT_INDEX = "/index.html";

    private final String method;
    private final String targetUri;
    private final String httpVersion;

    public HttpRequestStartLine(String line) {
        if(Objects.isNull(line)){
            throw new IllegalArgumentException();
        }
        String[] tokens = parseStartLine(line);

        this.method = tokens[0];
        this.targetUri = tokens[1].equals(ROOT_URI)? ROOT_INDEX : tokens[1];
        this.httpVersion = tokens[2];
    }

    private String[] parseStartLine(String line){
        return  HttpRequestUtils.parseHttpRequestStartLine(line);
    }
    public String getMethod() {
        return method;
    }

    public String getTargetUri() {
        return targetUri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
