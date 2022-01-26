package controller.request;

import util.HttpStatus;

import java.util.List;

/**
 * Created by melodist
 * Date: 2022-01-25 025
 * Time: 오후 6:33
 */
public class Request {
    private HttpStatus httpStatus;
    private final RequestLine requestLine;
    private final RequestHeader requestHeader;
    private final RequestBody requestBody;

    public Request(RequestLine requestLine, RequestHeader requestHeader, RequestBody requestBody) {
        this.requestLine = requestLine;
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
    }

    public static Request from(String requestLineString,
                               List<String> requestHeaderStrings,
                               String requestBodyString) {
        RequestLine requestLine = RequestLine.from(requestLineString);
        RequestHeader requestHeader = RequestHeader.from(requestHeaderStrings);
        RequestBody requestBody = RequestBody.from(requestBodyString);

        return new Request(requestLine, requestHeader, requestBody);
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
