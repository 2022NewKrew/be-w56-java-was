package webserver.controller.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.h2.util.StringUtils;

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

    public static HttpRequest from(InputStream in) throws IOException, URISyntaxException {

        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // Parse Request Line
        RequestLine requestLine = RequestLine.from(br.readLine());

        // Parse Request Header
        validateBufferedReader(br);
        RequestHeader requestHeader = RequestHeader.from(br);

        // Parse request Body
        String contentLength = requestHeader.get("Content-Length");
        if (StringUtils.isNullOrEmpty(contentLength) || !StringUtils.isNumber(contentLength)) {
            return new HttpRequest(requestLine, requestHeader, RequestBody.of());
        }
        RequestBody requestBody = RequestBody.of(br, Integer.parseInt(contentLength));

        return new HttpRequest(requestLine, requestHeader, requestBody);
    }

    private static void validateBufferedReader(BufferedReader br) throws IOException {
        if (!br.ready()) {
            throw new IOException("입력 스트림이 준비되지 않은 입력입니다.(Postman 에서 요청 시 발생)");
        }
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
