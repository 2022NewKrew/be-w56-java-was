package http.request;

import lombok.Getter;

@Getter
public class HttpRequestLine {
    private String method;
    private String url;
    private String version;

    public HttpRequestLine(String requestLine) {
        String[] splits = requestLine.split(" ");
        if (splits.length != 3) {
            throw new IllegalArgumentException("invalid http request line");
        }
        this.method = splits[0];
        this.url = splits[1];
        this.version = splits[2];
    }
}
