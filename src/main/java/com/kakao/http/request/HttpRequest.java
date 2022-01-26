package com.kakao.http.request;

import com.kakao.http.header.BasicHeader;
import com.kakao.http.header.HttpHeader;
import com.kakao.util.HttpRequestUtils;
import com.kakao.util.IOUtils;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
public class HttpRequest {
    private final HttpMethod method;
    private final Url url;
    private final String version;
    private final List<HttpHeader> headers;
    private final String body;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        HttpRequestLine requestLine = new HttpRequestLine(br.readLine());
        this.method = HttpMethod.valueOf(requestLine.getMethod());
        this.url = new Url(requestLine.getUrl());
        this.version = requestLine.getVersion();
        this.headers = parseHeaderList(br);
        this.body = parseRequestBody(br);
    }

    private String parseRequestBody(BufferedReader br) throws IOException {
        Optional<HttpHeader> contentLengthHeader = this.headers.stream()
                .filter(header -> header.key().equalsIgnoreCase("Content-Length"))
                .findFirst();
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
