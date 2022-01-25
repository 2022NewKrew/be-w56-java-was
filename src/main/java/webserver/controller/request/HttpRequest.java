package webserver.controller.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import webserver.common.HttpMethod;

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

        // Request Line
        RequestLine requestLine = RequestLine.from(br.readLine());
        if (!br.ready()) {
            throw new IOException("입력 스트림이 준비되지 않은 입력입니다.(Postman 에서 요청 시에 주로 발생합니다.)");
        }

        // Request Header
        RequestHeader requestHeader = RequestHeader.from(br);

        // Request Body
        if (requestLine.getMethod() == HttpMethod.GET || !br.ready()) {
            return new HttpRequest(requestLine, requestHeader, RequestBody.of());
        }
        RequestBody requestBody = RequestBody.of(br);

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
