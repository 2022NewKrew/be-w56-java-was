package com.kakao.http.response;

import com.kakao.http.header.HttpHeader;
import com.kakao.http.header.LocationHeader;

import java.util.ArrayList;
import java.util.List;

import static com.kakao.webserver.WebServerConfig.DEFAULT_HTTP_VERSION;

public class HttpResponseUtils {

    public static HttpResponse redirectTo(String url, List<HttpHeader> headers) {
        List<HttpHeader> appendedHeaders = new ArrayList<>(headers);
        appendedHeaders.add(new LocationHeader(url));
        return new HttpResponse(DEFAULT_HTTP_VERSION, HttpStatus.FOUND, appendedHeaders, null);
    }

    public static HttpResponse redirectTo(String url) {
        List<HttpHeader> headers = List.of(new LocationHeader(url));
        return new HttpResponse(DEFAULT_HTTP_VERSION, HttpStatus.FOUND, headers, null);
    }
}
