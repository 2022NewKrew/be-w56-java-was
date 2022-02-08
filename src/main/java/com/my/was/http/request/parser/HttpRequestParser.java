package com.my.was.http.request.parser;

import com.my.was.http.HttpHeaders;
import com.my.was.util.IOUtils;
import com.my.was.http.request.HttpRequest;
import com.my.was.http.request.HttpRequestLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequestParser {

    public HttpRequest parse(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        HttpRequestLine httpRequestLine = parseRequestLine(br);
        HttpHeaders httpRequestHeader = parseRequestHeaderAfterParsingRequestLine(br);
        String httpRequestBody = httpRequestHeader.getHeader("Content-Length")
                .map(contentLength -> parseRequestBodyAfterParsingRequestHeader(br, Integer.parseInt(contentLength)))
                .orElse("");

        return new HttpRequest(httpRequestLine, httpRequestHeader, httpRequestBody);
    }

    private HttpRequestLine parseRequestLine(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        if (requestLine == null) {
            throw new IllegalArgumentException("Request Header가 정상적이지 않습니다.");
        }

        return new HttpRequestLine(requestLine);
    }

    private HttpHeaders parseRequestHeaderAfterParsingRequestLine(BufferedReader br) throws IOException {
        HttpHeaders httpRequestHeader = new HttpHeaders();
        String header = br.readLine();
        while (!"".equals(header) && header != null) {
            String headerName = header.substring(0, header.indexOf(":"));
            String headerContent = header.substring(header.indexOf(":") + 2);
            httpRequestHeader.addHeader(headerName, headerContent);
            header = br.readLine();
        }

        return httpRequestHeader;
    }

    private String parseRequestBodyAfterParsingRequestHeader(BufferedReader br, int contentLength)  {
        return IOUtils.readData(br, contentLength);
    }

}
