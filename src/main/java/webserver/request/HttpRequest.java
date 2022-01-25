package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.google.common.collect.Maps;

import util.HttpRequestUtils;

public class HttpRequest {
    private RequestLine requestLine;
    private RequestHeader requestHeader;
    private RequestBody requestBody;

    private HttpRequest(RequestLine requestLine, RequestHeader requestHeader,
                        RequestBody requestBody) {
        this.requestLine = requestLine;
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
    }

    public static HttpRequest from(InputStream in) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        RequestLine requestLine = RequestLine.from(br.readLine());
        if (requestLine == null) {
            return null;
        }

        RequestHeader requestHeader = RequestHeader.from(br);
        if ("GET".equals(requestLine.getMethod())) {
            return new HttpRequest(requestLine, requestHeader, new RequestBody());
        }

        RequestBody requestBody = RequestBody.of(br);

        return new HttpRequest(requestLine, requestHeader, requestBody);
    }


    public String getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getQueryStringParams(String key) {
        return requestHeader.get(key);
    }

    public String getBodyParams(String key) {
        return requestBody.get(key);
    }
}
