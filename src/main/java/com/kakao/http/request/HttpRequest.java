package com.kakao.http.request;

import com.kakao.http.common.HttpCookie;
import com.kakao.http.header.BasicHeader;
import com.kakao.http.header.HttpHeader;
import com.kakao.util.HttpRequestUtils;
import com.kakao.util.IOUtils;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Getter
public class HttpRequest {
    private final HttpMethod method;
    private final Url url;
    private final String version;
    private final HttpHeaderStorage headerStorage;
    private final HttpCookieStorage cookieStorage;
    private final String rawBody;
    private final Map<String, String> bodyMap;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        HttpRequestLine requestLine = new HttpRequestLine(br.readLine());
        this.method = HttpMethod.valueOf(requestLine.getMethod());
        this.url = new Url(requestLine.getUrl());
        this.version = requestLine.getVersion();
        this.headerStorage = new HttpHeaderStorage(parseHeaderList(br));
        this.cookieStorage = this.headerStorage.findByName("Cookie")
                .map(HttpCookieStorage::new)
                .orElseGet(HttpCookieStorage::new);
        this.rawBody = parseRequestBody(br);
        this.bodyMap = parseBodyMap(this.rawBody);
    }

    public String findUrlParam(String field) {
        return this.url.getQueryMap().get(field);
    }

    public String findBodyParam(String key) {
        return this.bodyMap.get(key);
    }

    public Optional<HttpCookie> findCookieByName(String name) {
        return this.cookieStorage.findByName(name);
    }

    private Map<String, String> parseBodyMap(String rawBody) {
        if (rawBody == null || rawBody.isEmpty()) {
            return Collections.emptyMap();
        }
        return HttpRequestUtils.parseQueryString(rawBody);
    }

    private String parseRequestBody(BufferedReader br) throws IOException {
        Optional<HttpHeader> contentLengthHeader = this.getHeaderStorage().findByName("Content-Length");
        return contentLengthHeader.isEmpty()
                ? null
                : IOUtils.readData(br, Integer.parseInt(contentLengthHeader.get().value()));
    }

    private List<HttpHeader> parseHeaderList(BufferedReader bufferedReader) throws IOException {
        List<HttpHeader> headers = new ArrayList<>();
        String headerLine = bufferedReader.readLine();
        while (headerLine != null && !"".equals(headerLine)) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(headerLine);
            headers.add(new BasicHeader(pair.getKey(), pair.getValue()));
            headerLine = bufferedReader.readLine();
        }
        return Collections.unmodifiableList(headers);
    }
}
