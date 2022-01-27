package webserver.controller.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.h2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.common.HttpMethod;
import webserver.controller.Controller;

public class HttpRequest {
    private RequestLine requestLine;
    private RequestHeader requestHeader;
    private RequestBody requestBody;

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private HttpRequest(RequestLine requestLine, RequestHeader requestHeader,
                        RequestBody requestBody) {
        this.requestLine = requestLine;
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
    }

    public static HttpRequest from(InputStream in) throws IOException, URISyntaxException {

        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        RequestLine requestLine = RequestLine.from(br.readLine());
        if (!br.ready()) {
            throw new IOException("입력 스트림이 준비되지 않은 입력입니다.(Postman 에서 요청 시 발생)");
        }

        // Request Header
        RequestHeader requestHeader = RequestHeader.from(br);

        // Request Body
        String contentLength = requestHeader.get("Content-Length");
        if (StringUtils.isNullOrEmpty(contentLength) || !StringUtils.isNumber(contentLength)) {
            return new HttpRequest(requestLine, requestHeader, RequestBody.of());
        }
        int length = Integer.parseInt(contentLength);
        RequestBody requestBody = RequestBody.of(br, length);

        return new HttpRequest(requestLine, requestHeader, requestBody);
    }

    // 테스트메서드 용 생성 펙토리
    public static HttpRequest of(RequestLine requestLine, RequestHeader requestHeader,
                                 RequestBody requestBody) {
        return new HttpRequest(requestLine, requestHeader, requestBody);
    }


    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getQueryStringParams(String key) {
        return requestLine.getQueryStringParams(key);
    }

    public String getBodyParams(String key) {
        return requestBody.get(key);
    }
}
