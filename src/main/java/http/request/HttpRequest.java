package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequest {

    private StartLine startLine;

    private RequestHeaders headers;

    private RequestBody body;

    private RequestParams params;

    private HttpRequest(StartLine startLine, RequestHeaders headers, RequestBody body, RequestParams params) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
        this.params = params;
    }

    /**
     * To-do : parsing 에러가 발생할 경우 500 Error 를 발생시켜 예외 처리할 것
     */
    public static HttpRequest createHttpRequest(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StartLine startLine = StartLine.createStartLine(br);
        RequestHeaders header = RequestHeaders.createRequestHeader(br);
        String contentLength = header.getHeader("Content-Length");
        if(!"".equals(contentLength) && Integer.valueOf(contentLength) >= 0) {
        }
        RequestParams params = RequestParams.createRequestParams(startLine.getUrl().getQueryString());
        return new HttpRequest(startLine, header, null, params);
    }

    public String getUrl() {
        return startLine.getUrl().getPath();
    }

    public String getMethod() {
        return startLine.getMethod();
    }

    public String getHttpVersion() {
        return startLine.getHttpVersion();
    }

    public RequestHeaders getHeaders() {
        return headers;
    }

    public RequestBody getBody() {
        return body;
    }

    public RequestParams getParams() {
        return params;
    }
}
