package webserver.http.request;

import lombok.Getter;
import util.IOUtils;
import webserver.http.request.body.RequestBody;
import webserver.http.request.header.RequestHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Getter
public class HttpRequest {
    private final RequestHeader requestHeader;
    private final RequestBody requestBody;
    private final InfoMap infoMap;

    public HttpRequest(RequestHeader requestHeader, RequestBody requestBody, InfoMap infoMap) {
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
        this.infoMap = infoMap;
    }

    public static HttpRequest from(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        RequestHeader requestHeader = new RequestHeader(IOUtils.getHttpRequestHeader(br));
        RequestBody requestBody = new RequestBody(IOUtils.gttHttpRequestBody(br, requestHeader.getContentLength()));
        InfoMap infoMap = new InfoMap(requestHeader, requestBody);
        return new HttpRequest(requestHeader, requestBody, infoMap);
    }

    public String getUrlPath() {
        return requestHeader.getUrlPath();
    }
}
