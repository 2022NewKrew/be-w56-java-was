package controller.request;

import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-25 025
 * Time: 오후 6:33
 */
public class Request {
    private RequestLine requestLine;

    public Request(RequestLine requestLine) {
        this.requestLine = requestLine;
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getContentType() {
        return requestLine.getContentType();
    }

    public String getQueryStringParams(String key) {
        return requestLine.getQueryStringParams(key);
    }

}
