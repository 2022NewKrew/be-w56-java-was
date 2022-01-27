package web.http.request;

import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
    private final HttpRequestLine httpRequestLine;
    private final HttpRequestHeaders httpRequestHeaders;
    private final HttpRequestBody httpRequestBody;

    public HttpRequest(BufferedReader br) throws IOException {
        this.httpRequestLine = IOUtils.readRequestLine(br);
        this.httpRequestHeaders = IOUtils.readRequestHeaders(br);
        this.httpRequestBody = assignBody(br);
    }
    public HttpRequestLine getHttpRequestLine() {
        return httpRequestLine;
    }

    public HttpRequestHeaders getHttpRequestHeaders() {
        return httpRequestHeaders;
    }

    public String getBodyData(){
        return httpRequestBody.getBody();
    }

    private HttpRequestBody assignBody(BufferedReader br) throws IOException {
        if(this.httpRequestHeaders.isHeader("Content-Length")){
            int contentsLength = Integer.parseInt(this.httpRequestHeaders.getHeaderValueByKey("Content-Length"));
            return IOUtils.readRequestBody(br, contentsLength);
        }
        return new HttpRequestBody();
    }
}
