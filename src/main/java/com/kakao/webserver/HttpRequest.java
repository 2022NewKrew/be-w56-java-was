package com.kakao.webserver;

import com.kakao.util.HttpRequestUtils;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpRequest {
    private final HttpMethod method;
    private final Url url;
    private final String version;
    private final Map<String, String> headerMap;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        HttpRequestLine requestLine = new HttpRequestLine(br.readLine());
        this.method = HttpMethod.valueOf(requestLine.getMethod());
        this.url = new Url(requestLine.getUrl());
        this.version = requestLine.getVersion();
        this.headerMap = parseHeaderMap(br);
    }

    private Map<String, String> parseHeaderMap(BufferedReader bufferedReader) throws IOException {
        Map<String, String> tempMap = new HashMap<>();
        String headerLine = bufferedReader.readLine();
        while (headerLine != null && !"".equals(headerLine)) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(headerLine);
            tempMap.put(pair.getKey(), pair.getValue());
            headerLine = bufferedReader.readLine();
        }
        return Collections.unmodifiableMap(tempMap);
    }
}
